package com.zhou.ucenterservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhou.commonutils.JwtUtils;
import com.zhou.commonutils.MD5;
import com.zhou.servicebase.exceptionhandler.ZhouException;
import com.zhou.ucenterservice.entity.UcenterMember;
import com.zhou.ucenterservice.entity.vo.LoginVO;
import com.zhou.ucenterservice.entity.vo.RegisterVO;
import com.zhou.ucenterservice.mapper.UcenterMemberMapper;
import com.zhou.ucenterservice.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author Zhou
 * @since 2022-07-21
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public void registerUser(RegisterVO registerVO) {
        //获取参数
        String mobile = registerVO.getMobile();
        String password = registerVO.getPassword();
        String nickname = registerVO.getNickname();
        String code = registerVO.getCode();
        //验证参数是否为空
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password) || StringUtils.isEmpty(nickname) || StringUtils.isEmpty(code)) {
            throw new ZhouException(20001,"手机号码/密码/昵称/验证码不能为空,注册失败!");
        }
        //获取redis中的验证码,判断验证码是否正确
        String templateCode = redisTemplate.opsForValue().get(mobile);
        if (!code.equals(templateCode)) {
            throw new ZhouException(20001,"验证码错误,注册失败!");
        }
        //判断手机号是否重复，表里面存在相同手机号不进行添加
        QueryWrapper<UcenterMember> ucenterMemberQueryWrapper = new QueryWrapper<>();
        ucenterMemberQueryWrapper.eq("mobile",mobile);
        Integer ret = baseMapper.selectCount(ucenterMemberQueryWrapper);
        if (ret > 0) {
            throw new ZhouException(20001,"不能重复注册!");
        }
        //进行注册
        UcenterMember ucenterMember = new UcenterMember();
        ucenterMember.setMobile(mobile);
        //密码进行加密处理
        ucenterMember.setPassword(MD5.encrypt(password));
        ucenterMember.setNickname(nickname);
        //默认头像
        ucenterMember.setAvatar("http://edu-projects-zhou.oss-ap-northeast-1.aliyuncs.com/2022/06/23/858db430bbf54d668bf8160a0dd5933afile.png");
        ucenterMember.setIsDeleted(false);
        baseMapper.insert(ucenterMember);
    }

    @Override
    public String login(LoginVO loginVO) {
        //获取参数
        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();
        //非空判断
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new ZhouException(20001,"手机号码/密码不能为空,登录失败!");
        }
        //判断手机号是否正确
        QueryWrapper<UcenterMember> ucenterMemberQueryWrapper = new QueryWrapper<>();
        ucenterMemberQueryWrapper.eq("mobile",mobile);
        UcenterMember ucenterMember = baseMapper.selectOne(ucenterMemberQueryWrapper);
        if (ucenterMember == null) {
            throw new ZhouException(20001,"该用户不存在,登录失败!");
        }
        //判断密码
        //因为存储到数据库密码肯定加密的
        //把输入的密码进行加密，再和数据库密码进行比较
        //加密方式 MD5
        String pwByMD5 = MD5.encrypt(password);
        //系统记录的密码
        String pwInUM = ucenterMember.getPassword();
        if (!pwInUM.equals(pwByMD5)) {
            throw  new ZhouException(20001,"密码错误,登录失败!");
        }
        //登录成功
        //生成token字符串，使用jwt工具类
        String token = JwtUtils.getJwtToken(ucenterMember.getId(), ucenterMember.getNickname());
        return token;
    }

    @Override
    public UcenterMember getMemberInfo(HttpServletRequest request) {
        //调用jwt工具类的方法。根据request对象获取头信息，返回用户id
        String id = JwtUtils.getMemberIdByJwtToken(request);
        //查询数据库根据用户id获取用户信息
        UcenterMember ucenterMember = baseMapper.selectById(id);
        return ucenterMember;
    }

    @Override
    public Integer getRegisterNumber(String date) {
        Integer ret = baseMapper.selectRegisterNumber(date);
        return ret;
    }
}
