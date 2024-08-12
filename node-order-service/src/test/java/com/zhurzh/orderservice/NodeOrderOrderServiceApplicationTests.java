package com.zhurzh.orderservice;

//import com.zhurzh.commonnodeservice.service.MainService;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonjpa.entity.Order;
import com.zhurzh.commonjpa.enums.BackgroundOfIllustration;
import com.zhurzh.commonjpa.enums.CountOfPersons;
import com.zhurzh.commonjpa.enums.DetalizationOfIllustration;
import com.zhurzh.commonjpa.enums.FormatOfIllustration;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@SpringBootTest
//@ComponentScan(basePackages = {"com.zhurzh.commonnodeservice", "com.zhurzh.nodeorderservice"})
class NodeOrderOrderServiceApplicationTests {

//    @Autowired
////    @Qualifier("myCacheManager")
//    MyCacheManager userCache;

    @Test
    void contextLoads() throws InterruptedException {

        Order order = new Order();
        order.setName("name");
        order.setPrice("22");
        order.setDeadLine(Date.from(Instant.now()));
        order.setArtReference("set");
        order.setCommentToArt("comment");
                order.setOwner(AppUser.builder().language("eng").build());
                order.setCountOfPersons(CountOfPersons.TWO);
                order.setFormatOfIllustration(FormatOfIllustration.FULL_BODY);
                order.setBackgroundOfIllustration(BackgroundOfIllustration.DETAILED);
                order.setDetalizationOfIllustration(DetalizationOfIllustration.LINE_ART_SHADING);
        System.out.println("idea 1 detailed background: " + order.calculatePrice());
        System.out.println();

        order.setBackgroundOfIllustration(BackgroundOfIllustration.SIMPLE_WITH_ELEMENTS_OF_BLUR);
        System.out.println("idea 1 with simple background : " + order.calculatePrice());
        System.out.println();


        order.setBackgroundOfIllustration(BackgroundOfIllustration.BLURRED);
        order.setDetalizationOfIllustration(DetalizationOfIllustration.CLASSICAL);

        System.out.println("idea 2 no background: " + order.calculatePrice());


//        commandsManager.addButtonHelp(new ArrayList<>());
//        mainService.processDocMessage(null);
//
//        var map = UserStateController.getMapCopy();
//        for (var pair : map.entrySet()){
//            System.out.printf("\nkey : %s, val : %s", pair.getKey(), pair.getValue());
//        }
//        isCacheWorkCorrect();
//        isCacheDeleteAfterSetTime();

    }
//    void isCacheWorkCorrect() {
//        String a = "dfadfasdf23";
//
//        // Проверяем, что элемента еще нет в кэше
//        assertFalse(userCache.checkAndAdd(a), "It should be false at first time");
//
//        // Проверяем, что элемент теперь есть в кэше
//        assertTrue(userCache.checkAndAdd(a), "It should be true at second time");
//    }
//
//    void isCacheDeleteAfterSetTime() throws InterruptedException {
//        String a = "dfdfffdddsaaa";
//        // Ждем 5.5 секунды, чтобы истекло время кэша
//        assertFalse(userCache.checkAndAdd(a), "It should be false at first time");
//
//        Thread.sleep(6000);
//
//        // Проверяем, что элемент исчез из кэша после истечения времени
//        assertFalse(userCache.checkAndAdd(a), "It should be false after cache expiration");
//    }




}
