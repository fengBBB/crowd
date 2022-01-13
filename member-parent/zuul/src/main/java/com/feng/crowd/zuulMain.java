package com.feng.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringBootApplication
public class zuulMain {
    public static void main(String[] args) {
        SpringApplication.run(zuulMain.class,args);
    }
}
