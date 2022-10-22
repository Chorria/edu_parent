package com.zhou.statisticsservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling   //开启定时任务注解
@ComponentScan(basePackages = "com.zhou")
@MapperScan("com.zhou.statisticsservice.mapper")
public class StatisticsApplication {
    public static void main(String[] args){
        SpringApplication.run(StatisticsApplication.class,args);
    }
}
