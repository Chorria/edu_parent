package com.zhou.statisticsservice.client;

import com.zhou.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-ucenter")
@Component
public interface UcenterClient {
    @GetMapping("/eduucenter/member/getRegisterNumber/{date}")
    public R getRegisterNumber(@PathVariable("date") String date);
}
