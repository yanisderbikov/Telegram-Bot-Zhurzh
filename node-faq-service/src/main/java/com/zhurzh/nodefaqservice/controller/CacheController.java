package com.zhurzh.nodefaqservice.controller;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zhurzh.commonjpa.entity.AppUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class CacheController {
    @Value("${cache.expiration}")
    private int cacheExpiration;

    @Value("${cache.maxSize}")
    private int cacheMaxSize;

    Cache<Long, Long> lastViewedId;
    Cache<Long, Set<Long>> setViewed;
    Cache<Long, String> question;

    @PostConstruct
    public void init() {
        lastViewedId = CacheBuilder.newBuilder()
                .expireAfterWrite(cacheExpiration, TimeUnit.SECONDS)
                .maximumSize(cacheMaxSize)
                .build();
        setViewed = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.DAYS)
                .maximumSize(cacheMaxSize)
                .build();
        question =  CacheBuilder.newBuilder()
                .expireAfterWrite(cacheExpiration, TimeUnit.SECONDS)
                .maximumSize(cacheMaxSize)
                .build();
    }
    public String getQuestion(AppUser appUser){
        return question.getIfPresent(appUser.getId());
    }
    public void setQuestion(AppUser appUser, String q){
        question.put(appUser.getId(), q);
    }

    /**
     *
     * @param userId
     * @return возращает null если нет последнего просмотренного
     */
    public Long getLastViewedFAQ(Long userId) {
        return lastViewedId.getIfPresent(userId);
    }


    /**
     *
     * @param userId
     * @param faqId true если уникальный клик, false если уже кликал сюда
     *
     * обновляет посещенный вопрос.
     */
    public boolean updateLastViewedFAQ(Long userId, Long faqId) {
        lastViewedId.put(userId, faqId);
        var set = setViewed.getIfPresent(userId);
        if (set != null){
            if (set.contains(faqId)) return false;
            set.add(faqId);
            return true;
        }else {
            Set<Long> tempSet = new HashSet<>();
            tempSet.add(faqId);
            setViewed.put(userId, tempSet);
            return false;
        }
    }
}
