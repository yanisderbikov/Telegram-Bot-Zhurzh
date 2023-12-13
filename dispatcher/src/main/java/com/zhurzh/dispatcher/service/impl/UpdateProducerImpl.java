package com.zhurzh.dispatcher.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.zhurzh.dispatcher.service.UpdateProducer;

@Service
@Log4j
public class UpdateProducerImpl implements UpdateProducer {
    private final RabbitTemplate rabbitTemplate;
    private static Update prevUpdate;

    public UpdateProducerImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produce(String rabbitQueue, Update update) {
//        if (prevUpdate == null){
//            prevUpdate = update;
//        }else {
//            if (update.equals(prevUpdate)) return;
//        }

        if (update.hasCallbackQuery()){
            log.debug(update.getCallbackQuery());
        } else {
            log.debug(update.getMessage().getText());
        }
        rabbitTemplate.convertAndSend(rabbitQueue, update);
    }
}
