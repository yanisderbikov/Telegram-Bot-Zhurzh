package com.zhurzh.node.bot.branches.faq;

import com.zhurzh.commonnodeservice.service.impl.ConnectionToService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class FAQManager extends ConnectionToService {
    public FAQManager(@Value(  "${faq.service.callbackpath}") String c,
                        @Value("${faq.service.url}") String url){
        super(c, url);
    }
}
