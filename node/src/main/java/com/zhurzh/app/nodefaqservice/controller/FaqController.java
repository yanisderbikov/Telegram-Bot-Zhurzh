package com.zhurzh.app.nodefaqservice.controller;

import com.zhurzh.app.nodefaqservice.service.NodeFaqService;
import com.zhurzh.app.commonjpa.enums.BranchStatus;
import com.zhurzh.app.commonutils.model.Body;
import com.zhurzh.app.commonutils.model.Branch;
import com.zhurzh.app.nodefaqservice.enums.TextMessage;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;


@Log4j
@Component
public class FaqController extends Branch {

    private final NodeFaqService nodeFaqService;

    public FaqController(NodeFaqService nodeFaqService) {
        super(BranchStatus.FAQ);
        this.nodeFaqService = nodeFaqService;
    }

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
