package com.zhurzh.node.bot.branches.searem;

import com.zhurzh.node.bot.branches.ConnectionClass;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SeaRemBranch extends ConnectionClass {
    public SeaRemBranch(@Value("${searem.service.callbackpath}") String c,
                            @Value("${searem.service.url}") String url){
        super(c, url);
    }
}
