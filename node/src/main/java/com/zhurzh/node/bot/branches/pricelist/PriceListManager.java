package com.zhurzh.node.bot.branches.pricelist;

import com.zhurzh.node.bot.branches.ConnectionClass;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PriceListManager extends ConnectionClass {
    public PriceListManager(@Value("${pricelist.service.callbackpath}") String c,
                        @Value("${pricelist.service.url}") String url){
        super(c, url);
    }
}
