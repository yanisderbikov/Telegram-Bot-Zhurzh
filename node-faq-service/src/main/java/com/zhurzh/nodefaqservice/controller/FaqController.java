package com.zhurzh.nodefaqservice.controller;

import com.zhurzh.commonjpa.enums.BranchStatus;
import com.zhurzh.commonutils.model.Body;
import com.zhurzh.commonutils.model.Branch;
import com.zhurzh.commonutils.model.BranchesInterface;
import com.zhurzh.nodefaqservice.enums.TextMessage;
import com.zhurzh.nodefaqservice.service.NodeFaqService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
