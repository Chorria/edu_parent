package com.zhou.orderservice.controller;


import com.zhou.commonutils.R;
import com.zhou.orderservice.service.PayLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author Zhou
 * @since 2022-08-24
 */
@Api(description = "支付二维码管理")
@RestController
@RequestMapping("/eduorder/payLog")
@CrossOrigin
public class PayLogController {
    @Autowired
    private PayLogService payLogService;

    @ApiOperation("生成微信支付二维码接口")
    @GetMapping("/createPayQRCode/{orderNo}")
    public R createPayQRCode(@ApiParam(name = "orderNo",value = "课程id",required = true) @PathVariable String orderNo){
        //返回信息，包含二维码地址，还有其他需要的信息
        Map<String,Object> retMap = payLogService.createPayQRCode(orderNo);
        return R.ok().data(retMap);
    }

    @ApiOperation("查询订单状态接口")
    @GetMapping("/updateOrdersStatus/{orderNo}")
    public R updateOrdersStatus(@ApiParam(name = "orderNo",value = "支付订单编号",required = true)@PathVariable String orderNo){
        //查询订单状态
        Map<String,String> map = payLogService.queryOrderStatus(orderNo);
        if (map == null){
            return R.error().message("支付出现异常,请重新支付!");
        }
        //支付成功
        if ("SUCCESS".equals(map.get("trade_state"))) {
            payLogService.updateOrdersStatus(map);
            return R.ok().message("订单支付成功!");
        }
        return R.ok().code(25000).message("订单正在支付中......");
    }

}

