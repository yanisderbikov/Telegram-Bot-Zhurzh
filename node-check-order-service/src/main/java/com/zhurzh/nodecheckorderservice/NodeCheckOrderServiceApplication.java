package com.zhurzh.nodecheckorderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.zhurzh.commonnodeservice", "com.zhurzh.nodecheckorderservice"})
public class NodeCheckOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NodeCheckOrderServiceApplication.class, args);
    }

}
