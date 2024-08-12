package com.zhurzh.app.nodeorderservice.controller;

import com.zhurzh.app.nodeorderservice.enums.TextMessage;
import com.zhurzh.app.nodeorderservice.service.OrderService;
import com.zhurzh.app.commonjpa.enums.BranchStatus;
import com.zhurzh.app.commonutils.model.Body;
import com.zhurzh.app.commonutils.model.Branch;
import lombok.extern.log4j.Log4j;

import org.springframework.stereotype.Component;

@Log4j
@Component
public class OrderServiceController extends Branch {

    private final OrderService orderService;

    public OrderServiceController(OrderService orderService) {
        super(BranchStatus.ORDER);
        this.orderService = orderService;
    }

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
