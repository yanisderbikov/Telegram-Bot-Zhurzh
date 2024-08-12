package com.zhurzh.nodestartservice.controller;

import com.zhurzh.commonutils.model.Body;
import com.zhurzh.commonutils.model.BranchesInterface;
import com.zhurzh.nodestartservice.service.MainNodeStartService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Log4j
@AllArgsConstructor
@Component
public class StartController implements BranchesInterface {

    MainNodeStartService mainNodeStartService;

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