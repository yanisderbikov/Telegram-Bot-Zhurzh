package com.zhurzh.dispatcher.controller;

import com.zhurzh.dispatcher.service.UpdateProducer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.zhurzh.commonrabbitmq.model.RabbitQueue.GROUP_TEXT_MESSAGE_UPDATE;

@Component
@Log4j
@Getter
@AllArgsConstructor
public class UpdateProcessorGroup {

    private final TelegramBot telegramBot;
    private final UpdateProducer updateProducer;

    public void processUpdate(Update update) {
        updateProducer.produce(GROUP_TEXT_MESSAGE_UPDATE, update);
    }
}
