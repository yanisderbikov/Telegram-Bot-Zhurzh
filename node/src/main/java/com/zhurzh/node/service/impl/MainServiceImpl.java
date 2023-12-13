package com.zhurzh.node.service.impl;

import com.zhurzh.commonnodeservice.service.ProducerService;
import com.zhurzh.commonjpa.dao.AppUserDAO;
import com.zhurzh.node.bot.branches.BranchesManager;
import com.zhurzh.node.service.MainService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.springframework.stereotype.Service;


@Log4j
@Service
@AllArgsConstructor
public class MainServiceImpl implements MainService {
    private final BranchesManager branchesManager;

    @Override
    public void processCallBackMessage(Update update) {
        try {
            branchesManager.consumeCallBack(update);
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Override
    public void processTextMessage(Update update) { // ответы
        try {
            branchesManager.consumeText(update);
        } catch (Exception e) {
            log.error(e);
        }
    }


    @Override
    public void processDocMessage(Update update) {
//        var appUser = findOrSaveAppUser(update);
        var chatId = update.getMessage().getChatId();

    }

    @Override
    public void processPhotoMessage(Update update) {
//        var appUser = findOrSaveAppUser(update);
        var chatId = update.getMessage().getChatId();
//
    }


}
