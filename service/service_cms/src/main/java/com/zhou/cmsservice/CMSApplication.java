package com.zhou.cmsservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.zhou")
@MapperScan("com.zhou.cmsservice.mapper")
public class CMSApplication {
    public static void main(String[] args){
        SpringApplication.run(CMSApplication.class,args);
    }
}
