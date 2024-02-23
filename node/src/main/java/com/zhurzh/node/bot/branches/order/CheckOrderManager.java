package com.zhurzh.node.bot.branches.order;

import com.zhurzh.node.bot.branches.ConnectionClass;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CheckOrderManager extends ConnectionClass {
    public CheckOrderManager(@Value("${checkorder.service.callbackpath}") String callback,
            @Value("${checkorder.service.url}") String url){
        super(callback, url);
    }
}
