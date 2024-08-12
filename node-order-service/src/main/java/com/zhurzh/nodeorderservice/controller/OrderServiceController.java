package com.zhurzh.nodeorderservice.controller;

import com.zhurzh.commonjpa.enums.BranchStatus;
import com.zhurzh.commonutils.model.Body;
import com.zhurzh.commonutils.model.Branch;
import com.zhurzh.commonutils.model.BranchesInterface;
import com.zhurzh.nodeorderservice.enums.TextMessage;
import com.zhurzh.nodeorderservice.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
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
