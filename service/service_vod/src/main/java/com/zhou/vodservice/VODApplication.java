package com.zhou.vodservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient  //开启Nacos注解
@ComponentScan("com.zhou")
public class VODApplication {
    public static void main(String[] args){
        SpringApplication.run(VODApplication.class,args);
    }
}
