package com.zhurzh.nodecheckorderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@ComponentScan(basePackages = {"com.zhurzh.commonnodeservice", "com.zhurzh.nodecheckorderservice"})
public class NodeCheckOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NodeCheckOrderServiceApplication.class, args);
    }

}
