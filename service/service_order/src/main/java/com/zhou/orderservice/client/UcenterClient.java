package com.zhou.orderservice.client;

import com.zhou.commonutils.frontVO.UcenterMemberVO;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-ucenter")
@Component("ucenterClient")
public interface UcenterClient {
    @GetMapping("/eduucenter/member/getMemberInfoForOrder/{id}")
    public UcenterMemberVO getMemberInfoForOrder(@ApiParam(name = "id",value = "用户id",required = true) @PathVariable("id") String id);
}
