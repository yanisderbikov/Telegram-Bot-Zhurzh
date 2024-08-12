package com.zhurzh.orderservice.controller;

import com.zhurzh.commonutils.model.Body;
import com.zhurzh.orderservice.enums.TextMessage;
import com.zhurzh.orderservice.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

import com.zhurzh.commonutils.model.BranchesInterface;
import org.springframework.stereotype.Component;

@Log4j
@AllArgsConstructor
@Component
public class OrderServiceController implements BranchesInterface {

    private OrderService orderService;

    @Override
    public String isActiveAndGetButtonName(Body body){
        return TextMessage.ACTIVATION_BUTTON.getMessage(body.getAppUser().getLanguage());
    }
    @Override
    public void execute(Body body){
        try {
            orderService.execute(body.getAppUser(), body.getUpdate());
        }catch (Exception e){
            log.error(e);
        }
    }
}
