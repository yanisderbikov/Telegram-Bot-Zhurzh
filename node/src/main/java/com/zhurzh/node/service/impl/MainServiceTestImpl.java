package com.zhurzh.node.service.impl;

import com.zhurzh.node.service.MainService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@AllArgsConstructor
@Service
@Log4j
public class MainServiceTestImpl implements MainService {
    @Override
    public void processTextMessage(Update update) {
        log.debug("UpdateCome : " + update);
    }

    @Override
    public void processDocMessage(Update update) {
        log.debug("UpdateCome : " + update);

    }

    @Override
    public void processPhotoMessage(Update update) {
        log.debug("UpdateCome : " + update);

    }

    @Override
    public void processCallBackMessage(Update update) {
        log.debug("UpdateCome : " + update);
    }
}
