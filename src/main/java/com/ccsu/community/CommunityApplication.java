package com.ccsu.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 开启定时器
 * 开启线程异步支持
 * 扫描mapper层
 * springboot主程序
 * @author 华华
 */
@EnableScheduling
@EnableAsync
@MapperScan("com.ccsu.community.mapper")
@SpringBootApplication
public class CommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
    }

}



