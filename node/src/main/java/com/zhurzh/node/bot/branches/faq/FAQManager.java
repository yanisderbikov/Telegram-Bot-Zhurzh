package com.zhurzh.node.bot.branches.faq;

import com.zhurzh.node.bot.branches.ConnectionClass;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class FAQManager extends ConnectionClass{
    public FAQManager(@Value(  "${faq.service.callbackpath}") String c,
                        @Value("${faq.service.url}") String url,
                        @Value("${faq.service.port}") String port){
        super(c, url, port);
    }
}
