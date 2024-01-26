package com.zhurzh.nodeorderservice.ehcache;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.nodeorderservice.commands.StartCommand;
import com.zhurzh.nodeorderservice.controller.UserState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class MyCacheManager {
    private Cache<String, String> cache;
    private Cache<Long, UserState> stateCache;

    private Cache<Long, String> referenceCache;

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${cache.expiration}")
    private int cacheExpiration;

    @Value("${cache.maxSize}")
    private int cacheMaxSize;

    @PostConstruct
    public void init() {
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(cacheExpiration, TimeUnit.SECONDS)
                .maximumSize(cacheMaxSize)
                .build();
        stateCache = CacheBuilder.newBuilder()
                .expireAfterWrite(cacheExpiration, TimeUnit.SECONDS)
                .maximumSize(cacheMaxSize)
                .build();

        referenceCache = CacheBuilder.newBuilder()
                .expireAfterWrite(cacheExpiration, TimeUnit.SECONDS)
                .maximumSize(cacheMaxSize)
                .build();
    }


    public boolean checkAndAdd(String key) {
        if (cache.getIfPresent(key) != null) {
            return true;
        } else {
            cache.put(key, key);
            return false;
        }
    }

    public void setStateCache(AppUser appUser, UserState userState){
        stateCache.put(appUser.getId(), userState);
    }

    public UserState getStateCache(AppUser appUser){
        var us = stateCache.getIfPresent(appUser.getId());
        if (us != null){
            return us;
        }else {
            return setDefaultState(appUser);
        }
    }

    public UserState setDefaultState(AppUser appUser) {
        var us = applicationContext.getBean(StartCommand.class).getUserState();
        stateCache.put(appUser.getId(), us);
        return us;
    }

    public void setReferenceCache(AppUser appUser, String string){
        referenceCache.put(appUser.getId(), string);
    }

    public String getReferenceCache(AppUser appUser){
        var str = referenceCache.getIfPresent(appUser.getId());
        if (str == null) return "";
        return str;
    }
    public void clearReferenceCache(AppUser appUser){
        referenceCache.invalidate(appUser.getId());
    }
}
