package com.zhou.orderservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.wxpay.sdk.WXPayUtil;
import com.zhou.orderservice.entity.Order;
import com.zhou.orderservice.entity.PayLog;
import com.zhou.orderservice.mapper.PayLogMapper;
import com.zhou.orderservice.service.OrderService;
import com.zhou.orderservice.service.PayLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhou.orderservice.utils.HttpClient;
import com.zhou.servicebase.exceptionhandler.ZhouException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.Query;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author Zhou
 * @since 2022-08-24
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {
    @Autowired
    private OrderService orderService;

    @Override
    public Map<String, Object> createPayQRCode(String orderNo) {
        try {
            //1 查询课程订单
            QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
            orderQueryWrapper.eq("order_no",orderNo);
            Order order = orderService.getOne(orderQueryWrapper);

            // order.getTotalFee().multiply(new BigDecimal("100")).longValue()+""
            //2 封装生成支付二维码所需的参数
            Map sendMap = new HashMap<>();
            sendMap.put("appid","wx74862e0dfcf69954");  //应用id
            sendMap.put("mch_id", "1558950191");    //商户号
            sendMap.put("nonce_str", WXPayUtil.generateNonceStr()); //随机字符串
            sendMap.put("body",order.getCourseTitle()); //商品描述
            sendMap.put("out_trade_no", orderNo);   //商户订单号
            sendMap.put("total_fee",order.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");   //商品总金额
            sendMap.put("spbill_create_ip", "127.0.0.1");   //终端ip地址
            sendMap.put("notify_url", "http://localhost:8007/eduorder/payLog/weixinPay/weixinNotify\n");   //回调方法地址
            sendMap.put("trade_type", "NATIVE");    //交易类型
            System.out.println(sendMap);

            //3 发送httpclient请求,传递参数xml格式,微信支付提供的固定的地址
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            //设置xml格式的参数
            client.setXmlParam(WXPayUtil.generateSignedXml(sendMap,"T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            //发送post请求
            client.post();

            //获取返回参数
            String content = client.getContent();
            //返回参数从xml转换为map
            Map<String, String> responseMap = WXPayUtil.xmlToMap(content);

            //封装响应参数
            Map retMap = new HashMap();
            retMap.put("out_trade_no",orderNo);
            retMap.put("course_id",order.getCourseId());
            retMap.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");
            //返回二维码操作状态码
            retMap.put("result_code",responseMap.get("result_code"));
            //返回二维码地址
            retMap.put("code_url",responseMap.get("code_url"));

            return retMap;

        } catch (Exception e) {
            e.printStackTrace();
            throw new ZhouException(20001,"生成微信支付二维码失败!");
        }
    }
    //查询支付订单状态
    @Override
    public Map<String, String> queryOrderStatus(String orderNo) {
        try {
            //封装发送参数
            Map requestMap = new HashMap();
            requestMap.put("appid", "wx74862e0dfcf69954");
            requestMap.put("mch_id", "1558950191");
            requestMap.put("out_trade_no",orderNo);
            requestMap.put("nonce_str", WXPayUtil.generateNonceStr());
            //发送
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(requestMap,"T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();
            //返回参数
            String content = client.getContent();
            Map<String, String> responseMap = WXPayUtil.xmlToMap(content);

            return responseMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //更新支付记录和订单状态
    @Override
    public void updateOrdersStatus(Map<String, String> map) {
        //更新订单状态
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("order_no",map.get("out_trade_no"));
        Order order = orderService.getOne(orderQueryWrapper);
        //若订单已支付
        if (order.getStatus().intValue() == 1) {
            return;
        }
        order.setStatus(1);
        orderService.updateById(order);
        //添加支付记录
        PayLog payLog = new PayLog();
        payLog.setOrderNo(map.get("out_trade_no")); //订单号
        payLog.setTotalFee(order.getTotalFee());    //支付金额
        payLog.setTransactionId(map.get("transaction_id")); //流水号
        payLog.setTradeState(map.get("trade_state"));   //支付状态
        payLog.setPayType(1);   //支付方式
        payLog.setAttr(JSONObject.toJSONString(map));   //其他属性

        baseMapper.insert(payLog);
    }
}
