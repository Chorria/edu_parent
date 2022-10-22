package com.zhou.orderservice.service.impl;

import com.zhou.commonutils.frontVO.CourseWebVOOrder;
import com.zhou.commonutils.frontVO.UcenterMemberVO;
import com.zhou.orderservice.client.EduClient;
import com.zhou.orderservice.client.UcenterClient;
import com.zhou.orderservice.entity.Order;
import com.zhou.orderservice.mapper.OrderMapper;
import com.zhou.orderservice.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhou.orderservice.utils.OrderNoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author Zhou
 * @since 2022-08-24
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    private EduClient eduClient;
    @Autowired
    private UcenterClient ucenterClient;

    @Override
    public String createOrder(String courseId, String memberId) {
        //获取课程信息
        CourseWebVOOrder courseWebVOOrder = eduClient.getCourseInfoForOrder(courseId);

        //获取用户信息
        UcenterMemberVO ucenterMemberVO = ucenterClient.getMemberInfoForOrder(memberId);

        Order order = new Order();
        //生成订单号
        String orderNo = OrderNoUtil.getOrderNo();
        order.setOrderNo(orderNo);
        order.setCourseId(courseId);
        order.setCourseTitle(courseWebVOOrder.getTitle());
        order.setCourseCover(courseWebVOOrder.getCover());
        order.setTeacherName(courseWebVOOrder.getTeacherName());
        order.setMemberId(memberId);
        order.setNickname(ucenterMemberVO.getNickname());
        order.setMobile(ucenterMemberVO.getMobile());
        order.setPayType(1);  //支付类型 ，微信1
        order.setStatus(0);  //订单状态（0：未支付 1：已支付）
        //生成订单
        baseMapper.insert(order);

        return orderNo;
    }
}
