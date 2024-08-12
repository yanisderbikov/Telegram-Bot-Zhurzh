package com.zhurzh.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
//@ComponentScan(basePackages = {"com.zhurzh.app.commonnodeservice",
//        "com.zhurzh.node",
////        "com.zhurzh.app.nodecheckorderservice",
//        "com.zhurzh.nodefaqservice",
//        "com.zhurzh.nodeorderservice",
//        "com.zhurzh.nodepricelist",
//        "com.zhurzh.app.nodestartservice",
//})
public class NodeApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(NodeApplication.class);
        // Указываете путь к файлу, куда будет записан PID
        String pidFilePath = "pids/application-node-main.pid";
        app.addListeners(new ApplicationPidFileWriter(pidFilePath));
        app.run(args);
    }
}
