package com.zhurzh.node.service.impl;

import com.zhurzh.node.bot.branches.BranchesManagerInterface;
import com.zhurzh.node.service.ConsumerServiceRabbitMQ;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.zhurzh.commonrabbitmq.model.RabbitQueue.*;

@Service
@Log4j
@AllArgsConstructor
public class ConsumerServiceRabbitMQRabbitMQImpl implements ConsumerServiceRabbitMQ {
    private final BranchesManagerInterface branchesManagerInterface;

    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    public void consumeTextMessageUpdates(Update update) {
        branchesManagerInterface.consume(update);
    }


    @Override
    @RabbitListener(queues = CALLBACK_MESSAGE_UPDATE)
    public void consumeCallbackMessageUpdates(Update update) {
        branchesManagerInterface.consume(update);
    }
}
