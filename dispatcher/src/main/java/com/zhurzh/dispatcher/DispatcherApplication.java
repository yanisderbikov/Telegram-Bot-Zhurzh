package com.zhurzh.dispatcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DispatcherApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(DispatcherApplication.class);
        // Указываете путь к файлу, куда будет записан PID
        String pidFilePath = "pids/application-node-dispatcher.pid";
        app.addListeners(new ApplicationPidFileWriter(pidFilePath));
        app.run(args);
    }
}
