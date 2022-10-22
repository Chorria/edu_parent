package com.zhou.orderservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient  //开启Nacos注解
@EnableFeignClients //开启Feign注解
@ComponentScan(basePackages = "com.zhou")
@MapperScan("com.zhou.orderservice.mapper")
public class OrderApplication {
    public static void main(String[] args){
        SpringApplication.run(OrderApplication.class,args);
    }
}
