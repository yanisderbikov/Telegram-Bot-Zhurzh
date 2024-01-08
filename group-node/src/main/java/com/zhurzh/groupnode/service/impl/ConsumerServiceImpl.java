package com.zhurzh.groupnode.service.impl;


import com.zhurzh.groupnode.service.ConsumerService;
import com.zhurzh.groupnode.service.MainService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.zhurzh.commonrabbitmq.model.RabbitQueue.*;

@Service
@Log4j
@AllArgsConstructor
public class ConsumerServiceImpl implements ConsumerService {
    private final MainService mainService;

    @Override
    @RabbitListener(queues = GROUP_TEXT_MESSAGE_UPDATE)
    public void consumeTextMessageUpdates(Update update) {
        log.debug("NODE: Group text message is received");
        mainService.processTextMessage(update);
    }
}
