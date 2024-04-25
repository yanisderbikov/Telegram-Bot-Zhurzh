package com.zhurzh.node.bot.branches.searem;

import com.zhurzh.commonnodeservice.service.impl.ConnectionToService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SeaRemBranch extends ConnectionToService {
    public SeaRemBranch(@Value("${searem.service.callbackpath}") String c,
                            @Value("${searem.service.url}") String url){
        super(c, url);
    }
}
