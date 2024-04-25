package com.zhurzh.node.bot.branches.start;

import com.zhurzh.commonnodeservice.service.impl.ConnectionToService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StartManager extends ConnectionToService {
    public StartManager(@Value("${start.service.callbackpath}") String s,
            @Value("${start.service.url}") String url){
        super(s, url);
    }
}
