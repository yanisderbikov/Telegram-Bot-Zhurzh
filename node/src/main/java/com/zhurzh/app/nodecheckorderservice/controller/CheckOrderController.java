package com.zhurzh.app.nodecheckorderservice.controller;

import com.zhurzh.app.commonutils.model.Body;
import com.zhurzh.app.commonutils.model.BranchesInterface;
import com.zhurzh.app.nodecheckorderservice.enums.TextMessage;
import com.zhurzh.app.nodecheckorderservice.service.CheckOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Log4j
@AllArgsConstructor
@Component
public class CheckOrderController implements BranchesInterface {

    private CheckOrderService checkOrderService;

    @Override
    public String isActiveAndGetButtonName(@RequestBody Body body) {
        return TextMessage.ACTIVATION_BUTTON.getMessage(body.getAppUser().getLanguage());
    }

    @Override
    public void execute(@RequestBody Body body){
        checkOrderService.mange(body.getAppUser(), body.getUpdate());
    }
}
