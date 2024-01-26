package com.zhurzh.nodeorderservice;

//import com.zhurzh.commonnodeservice.service.MainService;
import com.zhurzh.commonjpa.dao.OrderDAO;
import com.zhurzh.commonjpa.entity.Order;
import com.zhurzh.nodeorderservice.controller.UserStateController;
import com.zhurzh.nodeorderservice.ehcache.MyCacheManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ComponentScan(basePackages = {"com.zhurzh.commonnodeservice", "com.zhurzh.nodeorderservice"})
class NodeOrderOrderServiceApplicationTests {

    @Autowired
//    @Qualifier("myCacheManager")
    MyCacheManager userCache;

    @Autowired
    OrderDAO orderDAO;

    @Test
    void contextLoads() throws InterruptedException {
//        commandsManager.addButtonHelp(new ArrayList<>());
//        mainService.processDocMessage(null);

        var map = UserStateController.getMapCopy();
        for (var pair : map.entrySet()){
            System.out.printf("\nkey : %s, val : %s", pair.getKey(), pair.getValue());
        }
        isCacheWorkCorrect();
        getAllConnectionsFiles();
//        isCacheDeleteAfterSetTime();

    }
    void isCacheWorkCorrect() {
        String a = "dfadfasdf23";

        // Проверяем, что элемента еще нет в кэше
        assertFalse(userCache.checkAndAdd(a), "It should be false at first time");

        // Проверяем, что элемент теперь есть в кэше
        assertTrue(userCache.checkAndAdd(a), "It should be true at second time");
    }

    void isCacheDeleteAfterSetTime() throws InterruptedException {
        String a = "dfdfffdddsaaa";
        // Ждем 5.5 секунды, чтобы истекло время кэша
        assertFalse(userCache.checkAndAdd(a), "It should be false at first time");

        Thread.sleep(6000);

        // Проверяем, что элемент исчез из кэша после истечения времени
        assertFalse(userCache.checkAndAdd(a), "It should be false after cache expiration");
    }

    void getAllConnectionsFiles(){
        var orders = orderDAO.findAll().stream().filter(Order::isAllFilled).toList();
        for (var order : orders){
            var name = order.getName();
            var ownerTelegram = order.getOwner().getTelegramUserName();
            var files = new StringBuilder();
            order.getArtReference();
            System.out.println("\n\nName : " + name);
            System.out.println("Owner : " + ownerTelegram);
            System.out.println("Files : " + files);
        }
    }



}
