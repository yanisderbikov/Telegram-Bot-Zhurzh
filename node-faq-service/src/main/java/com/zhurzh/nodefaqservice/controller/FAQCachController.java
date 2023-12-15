package com.zhurzh.nodefaqservice.controller;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class FAQCachController {

    @Cacheable(value = "lastViewedFAQ", key = "#userId")
    public Long getLastViewedFAQ(Long userId) {
        // Этот метод теперь ничего не возвращает.
        // Кэш будет заполнен только через `updateLastViewedFAQ`.
        return null;
    }

    @CachePut(value = "lastViewedFAQ", key = "#userId")
    public Optional<Long> updateLastViewedFAQ(Long userId, Long faqId) {
        return Optional.ofNullable(faqId); // Возвращает Optional с faqId, или Optional.empty(), если faqId равен null
    }

    @Cacheable(value = "uniqueViews", key = "#faqId")
    public Set<Long> getUniqueViewers(Long faqId) {
        return new HashSet<>();
    }

    @CachePut(value = "uniqueViews", key = "#faqId")
    public Set<Long> registerUniqueViewer(Long faqId, Long userId) {
        Set<Long> viewers = getUniqueViewers(faqId);
        viewers.add(userId);
        return viewers;
    }
}
