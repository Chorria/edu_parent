package com.zhou.eduservice.client;

import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-order")
public interface OrderClient {
    @GetMapping("/eduorder/order/isPay/{courseId}/{memberId}")
    public Boolean isPay(@PathVariable("courseId") String courseId, @PathVariable("memberId") String memberId);
}
