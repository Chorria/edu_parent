package com.zhou.eduservice.client;

import com.zhou.commonutils.frontVO.UcenterMemberVO;
import com.zhou.eduservice.client.impl.UcenterFileDegradeFeignClient;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-ucenter",fallback = UcenterFileDegradeFeignClient.class)
@Component("ucenterMemberClient")
public interface UcenterMemberClient {
    @GetMapping("/eduucenter/member/getMemberInfoById/{id}")
    public UcenterMemberVO getMemberInfoById(@ApiParam(name = "id",value = "用户id",required = true) @PathVariable("id") String id);
}
