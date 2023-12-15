package com.zhurzh.nodefaqservice.service;

import com.zhurzh.commonjpa.dao.FAQRepository;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonjpa.entity.FAQ;
import com.zhurzh.nodefaqservice.controller.FAQCachController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class FAQService {
    private final FAQRepository faqRepository;
    private FAQCachController cash;

    public List<FAQ> getFAQsSortedByPopularity(int limit, String language) {
        return faqRepository.findByLanguageAndAnswerIsNotNullOrderByPopularityScoreDesc(language, PageRequest.of(0, limit));
    }

    public Optional<FAQ> findById(Long id, AppUser appUser){
        var faqOpt = faqRepository.findById(id);
        faqOpt.ifPresent(faq -> registerClick(faq.getId(), appUser.getId()));
        return faqOpt;
    }

    public Optional<FAQ> getNextPopularFAQ(AppUser appUser) {
//        printCashe(appUser.getId());
        var idLastOpt = cash.getLastViewedFAQ(appUser.getId());

        if (idLastOpt != null) {
            Optional<FAQ> lastViewedFAQopt = faqRepository.findById(idLastOpt);
            if (lastViewedFAQopt.isPresent()) {
                FAQ lastViewedFAQ = lastViewedFAQopt.get();
                String language = appUser.getLanguage();
                Page<FAQ> page = faqRepository.findNextPopularFAQWithNonNullAnswer(lastViewedFAQ.getPopularityScore(), language, PageRequest.of(0, 1));

                if (!page.isEmpty()) {
                    FAQ nextFAQ = page.getContent().get(0);
                    registerClick(nextFAQ.getId(), appUser.getId());
                    cash.updateLastViewedFAQ(appUser.getId(), nextFAQ.getId());
                    return Optional.of(nextFAQ);
                }
            }
        }
        return Optional.empty();
    }



    public void updatePopularityScore(Long faqId) {
        FAQ faq = faqRepository.findById(faqId).orElse(null);
        if (faq != null) {
            int newPopularityScore = calculatePopularityScore(faq);
            faq.setPopularityScore(newPopularityScore);
            faqRepository.save(faq);
        }
    }

    public int getUniqueViewCount(Long faqId) {
        Set<Long> uniqueViewers = cash.getUniqueViewers(faqId);
        return uniqueViewers.size();
    }

    public boolean registerClick(Long faqId, Long userId) {
        Set<Long> uniqueViewers = cash.registerUniqueViewer(faqId, userId);
        FAQ faq = faqRepository.findById(faqId).orElse(null);
        if (faq != null) {
            faq.setViewCount(faq.getViewCount() + 1);
            faq.setPopularityScore(calculatePopularityScore(faq));
            var got = cash.updateLastViewedFAQ(userId, faqId);
            faqRepository.save(faq);
            log.debug("Click registered; user: {}; faq: {}", userId, faqId);
            return true;
        }
        log.debug("Click NOT registered; user: {}; faq: {}", userId, faqId);
        return false;
    }

    private int calculatePopularityScore(FAQ faq) {
        double viewCountWeight = 0.6;
        double uniqueViewWeight = 0.3;
        double timeFactorWeight = 0.1;

        int totalViews = faq.getViewCount();
        int uniqueViews = getUniqueViewCount(faq.getId());
        long daysSinceCreation = ChronoUnit.DAYS.between(getCreationDate(faq.getId()), LocalDate.now());

        double timeFactor = Math.max(0, 1 - (0.01 * daysSinceCreation));

        double popularityScore = (totalViews * viewCountWeight) +
                (uniqueViews * uniqueViewWeight) +
                (timeFactor * timeFactorWeight);

        return (int) popularityScore;
    }

    private LocalDate getCreationDate(Long faqId) {
        FAQ faq = faqRepository.findById(faqId).orElse(null);
        return (faq != null) ? faq.getCreationDate() : null;
    }

}
