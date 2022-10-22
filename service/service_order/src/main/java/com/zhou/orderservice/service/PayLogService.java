package com.zhou.orderservice.service;

import com.zhou.orderservice.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author Zhou
 * @since 2022-08-24
 */
public interface PayLogService extends IService<PayLog> {

    Map<String, Object> createPayQRCode(String orderNo);

    Map<String, String> queryOrderStatus(String orderNo);

    void updateOrdersStatus(Map<String, String> map);
}
