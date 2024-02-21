package com.zhurzh.nodecheckorderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.zhurzh.commonnodeservice", "com.zhurzh.nodecheckorderservice"})
public class NodeCheckOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(NodeCheckOrderServiceApplication.class);
        // Указываете путь к файлу, куда будет записан PID
        String pidFilePath = "pids/application-node-check-order.pid";
        app.addListeners(new ApplicationPidFileWriter(pidFilePath));
        app.run(args);
    }

}
