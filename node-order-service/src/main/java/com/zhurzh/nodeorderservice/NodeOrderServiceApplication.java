package com.zhurzh.nodeorderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.zhurzh.commonnodeservice", "com.zhurzh.nodeorderservice"})
@EnableCaching
public class NodeOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(NodeOrderServiceApplication.class);
        // Указываете путь к файлу, куда будет записан PID
        String pidFilePath = "pids/application-node-order.pid";
        app.addListeners(new ApplicationPidFileWriter(pidFilePath));
        app.run(args);
    }

}
