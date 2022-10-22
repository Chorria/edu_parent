package com.zhou.orderservice.client;

import com.zhou.commonutils.frontVO.CourseWebVOOrder;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-edu")
@Component("eduClient")
public interface EduClient {
    @GetMapping("/eduservice/courseFront/getCourseInfoForOrder/{id}")
    public CourseWebVOOrder getCourseInfoForOrder(@ApiParam(name = "id",value = "课程id",required = true) @PathVariable("id") String id);
}
