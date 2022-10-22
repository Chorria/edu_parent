package com.zhou.msmservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.zhou")
public class MSMApplication {
    public static void main(String[] args){
        SpringApplication.run(MSMApplication.class,args);
    }
}
