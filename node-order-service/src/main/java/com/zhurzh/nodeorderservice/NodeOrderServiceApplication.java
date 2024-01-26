package com.zhurzh.nodeorderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.zhurzh.commonnodeservice", "com.zhurzh.nodeorderservice"})
@EnableCaching
public class NodeOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NodeOrderServiceApplication.class, args);
    }

}
