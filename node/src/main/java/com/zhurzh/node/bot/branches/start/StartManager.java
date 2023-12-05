package com.zhurzh.node.bot.branches.start;

import com.zhurzh.node.bot.branches.ConnectionClass;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StartManager extends ConnectionClass {
    public StartManager(@Value("${start.service.callbackpath}") String s,
            @Value("${start.service.url}") String url,
                             @Value("${start.service.port}") String port){
        super(s, url, port);
    }
}
