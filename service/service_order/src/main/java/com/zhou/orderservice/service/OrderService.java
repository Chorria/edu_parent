package com.zhou.orderservice.service;

import com.zhou.orderservice.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author Zhou
 * @since 2022-08-24
 */
public interface OrderService extends IService<Order> {

    String createOrder(String courseId, String memberId);
}
