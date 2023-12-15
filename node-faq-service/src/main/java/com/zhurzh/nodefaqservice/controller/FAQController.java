package com.zhurzh.nodefaqservice.controller;

import com.zhurzh.commonjpa.entity.FAQ;
import com.zhurzh.nodefaqservice.service.FAQService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/faq")
@AllArgsConstructor
public class FAQController {
    // ... существующие поля и конструктор ...
    private FAQService faqService;

    @GetMapping("/popular")
    public List<FAQ> getPopularFAQs(@RequestParam(defaultValue = "10") int limit,
                                    @RequestParam(defaultValue = "eng") String language) {
        return faqService.getFAQsSortedByPopularity(limit, language);
    }

    // ... остальные методы ...
}