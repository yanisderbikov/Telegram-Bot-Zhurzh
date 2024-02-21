package com.zhurzh.nodefaqservice.service;

import com.zhurzh.commonjpa.dao.FAQRepository;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonjpa.entity.FAQ;
import com.zhurzh.nodefaqservice.controller.CacheController;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
//@Slf4j
@Log4j
@AllArgsConstructor
public class FAQService {
    private final FAQRepository faqRepository;
    private CacheController cash;

    public List<FAQ> getFAQsSortedByPopularity(int limit, String language) {
        return faqRepository.findByLanguageAndAnswerIsNotNullOrderByPopularityScoreDesc(language, PageRequest.of(0, limit));
    }

    public Optional<FAQ> findById(Long id, AppUser appUser){
        registerClick(id, appUser.getId());
        return faqRepository.findById(id);
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

    public void registerClick(Long faqId, Long userId) {
        if (cash.updateLastViewedFAQ(userId, faqId)){
            var faq = faqRepository.findById(faqId).orElseThrow();
            faq.setPopularityScore(faq.getPopularityScore() + 1);
            faqRepository.save(faq);
        }
    }
    @Scheduled(cron = "${cron.scheduler.calculate}")
    public void calculate(){
        var list = faqRepository.findAll().stream()
                .sorted(new Comparator<FAQ>() {
                    @Override
                    public int compare(FAQ o1, FAQ o2) {
                        return o2.getPopularityScore() - o1.getPopularityScore();
                    }
                }).toList();

        normalizeList(list);

        faqRepository.saveAll(list);
        StringBuilder builder = new StringBuilder();
        for (var faq : list) {
            builder.append(faq.getQuestion()).append(":").append(faq.getPopularityScore()).append("\n");
        }

        log.debug("Updated popularity");
        log.debug(builder.toString());
    }
    private void normalizeList(List<FAQ> list) {
        if (list.isEmpty()) return;

        int max = list.get(0).getPopularityScore();
        int min = list.get(list.size() - 1).getPopularityScore();

        // Нормализация значений
        if (max > Integer.MAX_VALUE) {
            int normalizationFactor = max / Integer.MAX_VALUE + 1;
            for (var faq : list) {
                faq.setPopularityScore(faq.getPopularityScore() / normalizationFactor);
            }
            // Обновляем минимальное значение после нормализации
            min = list.get(list.size() - 1).getPopularityScore();
        }

        // Дополнительная нормализация, чтобы масштаб значений начинался с 1
        if (min > 1) {
            for (var faq : list) {
                faq.setPopularityScore(faq.getPopularityScore() / min);
            }
        }
    }
}
