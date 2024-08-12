package com.zhurzh.nodefaqservice.controller;

import com.zhurzh.commonutils.model.Body;
import com.zhurzh.commonutils.model.BranchesInterface;
import com.zhurzh.nodefaqservice.enums.TextMessage;
import com.zhurzh.nodefaqservice.service.NodeFaqService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;


@Log4j
@AllArgsConstructor
@Component
public class FaqController implements BranchesInterface {

    private NodeFaqService nodeFaqService;

    @Override
    public String isActiveAndGetButtonName(@RequestBody Body body) {
        return TextMessage.ACTIVATION_BUTTON.getMessage(body.getAppUser().getLanguage());
    }


    @Override
    public void execute(@RequestBody Body body){
        try {
            nodeFaqService.mange(body.getAppUser(), body.getUpdate());
        }catch (Exception e){
            log.error(e);
        }
    }
}
