package com.zhurzh.groupnode.service.impl;

import com.zhurzh.groupnode.commands.GroupCommand;
import com.zhurzh.groupnode.commands.WelcomeCommand;
import com.zhurzh.groupnode.service.MainService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;


@Log4j
@Service
@AllArgsConstructor
public class MainServiceImpl implements MainService {

    GroupCommand groupCommand;
    @Override
    public void processTextMessage(Update update) { // ответы
        try {
            groupCommand.execute(update);
        } catch (Exception e) {
            log.error(e);
        }
    }




}
