package com.zhou.eduservice.client.impl;

import com.zhou.commonutils.frontVO.UcenterMemberVO;
import com.zhou.eduservice.client.UcenterMemberClient;
import com.zhou.servicebase.exceptionhandler.ZhouException;
import org.springframework.stereotype.Component;

/**
 * 熔断器实现类
 */
@Component
public class UcenterFileDegradeFeignClient implements UcenterMemberClient {
    @Override
    public UcenterMemberVO getMemberInfoById(String id) {
        throw new ZhouException(20001,"查询用户信息失败!");
    }
}
