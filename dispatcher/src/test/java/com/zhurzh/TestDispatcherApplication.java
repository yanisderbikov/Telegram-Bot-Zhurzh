package com.zhurzh;

import com.zhurzh.dispatcher.DispatcherApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

//@ComponentScan(basePackages = {"com.zhurzh.node"})
@SpringBootTest(classes = DispatcherApplication.class)
public class TestDispatcherApplication {


}
