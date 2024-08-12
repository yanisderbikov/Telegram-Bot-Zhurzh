package com.zhurzh.nodestartservice.controller;

import com.zhurzh.commonjpa.enums.BranchStatus;
import com.zhurzh.commonutils.model.Body;
import com.zhurzh.commonutils.model.Branch;
import com.zhurzh.commonutils.model.BranchesInterface;
import com.zhurzh.nodestartservice.service.MainNodeStartService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
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
    @PostMapping
    public String isActiveAndGetButtonName(@RequestBody Body body){
        return "The branch 'start service' is online";
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