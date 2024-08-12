package com.zhurzh.app.nodestartservice.controller;

import com.zhurzh.app.commonjpa.enums.BranchStatus;
import com.zhurzh.app.commonutils.model.Body;
import com.zhurzh.app.commonutils.model.Branch;
import com.zhurzh.app.nodestartservice.service.MainNodeStartService;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Log4j
@Component
public class StartController extends Branch {

    private final MainNodeStartService mainNodeStartService;

    public StartController( MainNodeStartService mainNodeStartService) {
        super(BranchStatus.START);
        this.mainNodeStartService = mainNodeStartService;
    }

    @Override
    public String isActiveAndGetButtonName(@RequestBody Body body){
        return null;
    }

    @Override
    public void execute(@RequestBody Body body){
        try {
            mainNodeStartService.execute(body.getAppUser(), body.getUpdate());
        }catch (Exception e){
            log.error(e);
        }
    }
}