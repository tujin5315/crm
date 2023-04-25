package com.msbtj.crm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
@MapperScan("com.msbtj.crm.dao")
public class Starter {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Starter.class);
        springApplication.run();
    }
}
