package com.zhurzh.node.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zhurzh.commonjpa.entity.AppUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.MessageId;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class UserMessageCache {
    private Cache<Long, Integer> cache;
    @Value(" ${cache.maxSize}")
    private int cacheMaxSize;

    @PostConstruct
    public void init() {
        int cacheExpiration = Integer.MAX_VALUE;
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(cacheExpiration, TimeUnit.SECONDS)
                .maximumSize(cacheMaxSize)
                .build();
    }

    public Integer getLastMessage(AppUser appUser){
        return cache.getIfPresent(appUser.getId());
    }

    public void setCache(AppUser appUser, Integer lastMessage){
        cache.put(appUser.getId(), lastMessage);
    }
}
