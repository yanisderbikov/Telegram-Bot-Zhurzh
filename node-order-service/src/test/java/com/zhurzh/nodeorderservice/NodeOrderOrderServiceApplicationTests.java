package com.zhurzh.nodeorderservice;

//import com.zhurzh.commonnodeservice.service.MainService;
import com.zhurzh.nodeorderservice.controller.UserStateController;
import com.zhurzh.nodeorderservice.service.CommonCommands;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@SpringBootTest
@ComponentScan(basePackages = {"com.zhurzh.commonnodeservice", "com.zhurzh.nodeorderservice"})
class NodeOrderOrderServiceApplicationTests {
//
//    @Autowired
//    CommandsManager commandsManager;
//    @Autowired
//    CommonCommands commonCommands;
//    @Autowired
//    MainService mainService;
    @Test
    void contextLoads() {
//        commandsManager.addButtonHelp(new ArrayList<>());
//        mainService.processDocMessage(null);

        var map = UserStateController.getMapCopy();
        for (var pair : map.entrySet()){
            System.out.printf("\nkey : %s, val : %s", pair.getKey(), pair.getValue());
        }
    }

}
