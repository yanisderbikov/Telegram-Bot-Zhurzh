package com.zhurzh.node.bot.branches.order;

import com.zhurzh.commonnodeservice.service.impl.ConnectionToService;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

/**
 * Устанавливает связь с сервисом node-order-service
 */
import org.springframework.beans.factory.annotation.Value;

@Component
@Log4j
public class OrderManager extends ConnectionToService {
    public OrderManager(@Value("${order.service.callbackpath}") String c,
            @Value("${order.service.url}") String url){
        super(c, url);
    }
}

