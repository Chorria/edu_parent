package com.zhou.orderservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhou.commonutils.JwtUtils;
import com.zhou.commonutils.R;
import com.zhou.orderservice.entity.Order;
import com.zhou.orderservice.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author Zhou
 * @since 2022-08-24
 */
@Api(description = "支付订单管理")
@RestController
@RequestMapping("/eduorder/order")
@CrossOrigin
public class OrderController {
    @Autowired
    private OrderService orderService;

    @ApiOperation("生成订单接口")
    @GetMapping("/createOrder/{courseId}")
    public R createOrder(@ApiParam(name = "courseId",value = "课程id",required = true) @PathVariable String courseId,
                         @ApiParam(name = "request",value = "请求实体",required = true) HttpServletRequest request){
        //返回订单号
        String orderNo = orderService.createOrder(courseId, JwtUtils.getMemberIdByJwtToken(request));
        return R.ok().data("orderNo",orderNo);
    }

    @ApiOperation("查询订单详情接口")
    @GetMapping("/getOrderInfo/{orderNo}")
    public R getOrderInfo(@ApiParam(name = "orderNo",value = "订单号",required = true) @PathVariable String orderNo){
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("order_no",orderNo);
        Order order = orderService.getOne(orderQueryWrapper);
        return R.ok().data("order",order);
    }

    @ApiOperation("查询订单是否支付")
    @GetMapping("/isPay/{courseId}/{memberId}")
    public Boolean isPay(@ApiParam(name = "courseId",value = "课程编号",required = true) @PathVariable String courseId,
                         @ApiParam(name = "memberId",value = "用户Id",required = true) @PathVariable String memberId){
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("course_id",courseId);
        orderQueryWrapper.eq("member_id",memberId);
        orderQueryWrapper.eq("status","1");

        int ret = orderService.count(orderQueryWrapper);
        if (ret > 0) {
            return true;
        } else {
            return false;
        }
    }

}

