package com.zhurzh.app.nodecheckorderservice.controller;

import com.zhurzh.app.commonjpa.enums.BranchStatus;
import com.zhurzh.app.commonutils.model.Body;
import com.zhurzh.app.commonutils.model.Branch;
import com.zhurzh.app.nodecheckorderservice.enums.TextMessage;
import com.zhurzh.app.nodecheckorderservice.service.CheckOrderService;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Log4j
@Component
public class CheckOrderController extends Branch {

    private final CheckOrderService checkOrderService;

    public CheckOrderController(CheckOrderService checkOrderService) {
        super(BranchStatus.CHECK_ORDER);
        this.checkOrderService = checkOrderService;
    }

    @Override
    public String isActiveAndGetButtonName(@RequestBody Body body) {
        return TextMessage.ACTIVATION_BUTTON.getMessage(body.getAppUser().getLanguage());
    }

    @Override
    public void execute(@RequestBody Body body){
        checkOrderService.mange(body.getAppUser(), body.getUpdate());
    }
}
