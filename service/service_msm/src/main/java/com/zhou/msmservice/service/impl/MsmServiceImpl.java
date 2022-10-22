package com.zhou.msmservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.zhou.msmservice.service.MsmService;
import com.zhou.servicebase.exceptionhandler.ZhouException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
public class MsmServiceImpl implements MsmService {
    @Override
    public boolean sendMsm(String phoneNumber, Map<String, Object> codeMap) {
        //判断手机号码是否为空
        if (StringUtils.isEmpty(phoneNumber)) {
            return false;
        }
        DefaultProfile profile = DefaultProfile.getProfile("default","LTAI5tDYP3GkaFVqHSsqQdQf","vv4vQ4cGjl6HecYSd5eSCuSDMypzxh");
        IAcsClient client = new DefaultAcsClient(profile);
        //设置相关固定的参数
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        //设置发送相关的参数
        request.putQueryParameter("PhoneNumbers",phoneNumber); //手机号
        request.putQueryParameter("SignName","阿里云短信测试"); //申请阿里云 签名名称
        request.putQueryParameter("TemplateCode","SMS_154950909"); //申请阿里云 模板code
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(codeMap)); //验证码数据，转换json数据传递

        try {
            //最终发送
            CommonResponse response = client.getCommonResponse(request);
            boolean success = response.getHttpResponse().isSuccess();
            return success;
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
