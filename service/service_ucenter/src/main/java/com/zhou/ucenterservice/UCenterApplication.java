package com.zhou.ucenterservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient  //开启Nacos注解
@ComponentScan("com.zhou")
@MapperScan("com.zhou.ucenterservice.mapper")
public class UCenterApplication {
    public static void main(String[] args){
        SpringApplication.run(UCenterApplication.class,args);
    }
}
