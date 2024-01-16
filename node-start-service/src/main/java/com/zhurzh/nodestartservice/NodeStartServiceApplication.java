package com.zhurzh.nodestartservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ComponentScan(basePackages = {"com.zhurzh.commonnodeservice", "com.zhurzh.nodestartservice", "com.zhurzh.commonjpa"})
@EnableAsync
public class NodeStartServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NodeStartServiceApplication.class, args);
    }

}
