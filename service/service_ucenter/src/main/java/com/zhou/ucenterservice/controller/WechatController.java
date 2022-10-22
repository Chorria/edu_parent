package com.zhou.ucenterservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.zhou.commonutils.JwtUtils;
import com.zhou.servicebase.exceptionhandler.ZhouException;
import com.zhou.ucenterservice.entity.UcenterMember;
import com.zhou.ucenterservice.service.UcenterMemberService;
import com.zhou.ucenterservice.utils.HttpClientUtils;
import com.zhou.ucenterservice.utils.WechatConstantUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@Api(description = "微信API管理")
@Controller
@CrossOrigin
@RequestMapping("/ucenter/wechat")
public class WechatController {
    @Autowired
    private UcenterMemberService ucenterMemberService;

    //1 生成微信扫描二维码
    @GetMapping("/loginQRCode")
    public String getWxQRCode(){
        // 微信开放平台授权baseUrl  %s相当于?代表占位符
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        //对redirect_url进行URLEncoder编码String
        String redirectUrl = WechatConstantUtil.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //设置%s里面值
        String url = String.format(baseUrl,WechatConstantUtil.WX_OPEN_APP_ID,redirectUrl,"zhou_edu");
        //重定向到请求微信地址里面
        return "redirect:" + url;
    }

    //2 获取扫描人信息，添加数据
    @GetMapping("/callback")
    public String getWechatInfo(String code,String state){
        try {
            //1 获取code值，临时票据，类似于验证码
            //2 拿着code请求 微信固定的地址，得到两个值 accsess_token 和 openid
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            //拼接三个参数 ：id  秘钥 和 code值
            String accessTokenUrl = String.format(baseAccessTokenUrl,WechatConstantUtil.WX_OPEN_APP_ID,WechatConstantUtil.WX_OPEN_APP_SECRET,code);

            //请求这个拼接好的地址，得到返回两个值 accsess_token 和 openid
            //使用httpclient发送请求，得到返回结果
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);

            //从accessTokenInfo字符串获取出来两个值 accsess_token 和 openid
            //把accessTokenInfo字符串转换map集合，根据map里面key获取对应值
            //使用json转换工具 Gson
            Gson gson = new Gson();
            HashMap accessInfoMap = gson.fromJson(accessTokenInfo, HashMap.class);
            String accessToken = (String) accessInfoMap.get("access_token");
            String openid = (String) accessInfoMap.get("openid");

            //把扫描人信息添加数据库里面
            //判断数据表里面是否存在相同微信信息，根据openid判断
            QueryWrapper<UcenterMember> ucenterMemberQueryWrapper = new QueryWrapper<>();
            ucenterMemberQueryWrapper.eq("openid",openid);
            UcenterMember ucenterMember = ucenterMemberService.getOne(ucenterMemberQueryWrapper);
            //没有在数据库内发现数据
            if (ucenterMember == null) {
                //3 拿着得到accsess_token 和 openid，再去请求微信提供固定的地址，获取到扫描人信息
                //访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                //拼接两个参数
                String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);
                //发送请求
                String userInfo = HttpClientUtils.get(userInfoUrl);
                //获取返回userinfo字符串扫描人信息
                HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
                //获取昵称
                String nickName = (String) userInfoMap.get("nickname");
                //获取头像
                String avatar = (String) userInfoMap.get("headimgurl");
                //获取性别
                Number sex = (Number) userInfoMap.get("sex");

                //根据微信信息新建用户
                ucenterMember = new UcenterMember();
                ucenterMember.setOpenid(openid);
                ucenterMember.setNickname(nickName);
                ucenterMember.setSex(sex.intValue());
                ucenterMember.setAvatar(avatar);
                ucenterMemberService.save(ucenterMember);
            }
            //使用jwt根据member对象生成token字符串
            String token = JwtUtils.getJwtToken(ucenterMember.getId(), ucenterMember.getNickname());
            //最后：返回首页面，通过路径传递token字符串
            return "redirect:http://localhost:3000?token=" + token;

        } catch (Exception e) {
            e.printStackTrace();
            throw new ZhouException(20001,"微信登录失败!");
        }
    }
}
