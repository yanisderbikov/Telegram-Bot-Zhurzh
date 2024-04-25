package com.zhurzh.node.bot.branches.pricelist;

import com.zhurzh.commonnodeservice.service.impl.ConnectionToService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PriceListManager extends ConnectionToService {
    public PriceListManager(@Value("${pricelist.service.callbackpath}") String c,
                        @Value("${pricelist.service.url}") String url){
        super(c, url);
    }
}
