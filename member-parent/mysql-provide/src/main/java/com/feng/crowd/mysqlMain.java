package com.feng.crowd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.feng.crowd.mapper")
@SpringBootApplication
public class mysqlMain {
    public static void main(String[] args) {
        SpringApplication.run(mysqlMain.class,args);
    }
}
