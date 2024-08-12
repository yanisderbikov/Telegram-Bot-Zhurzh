package com.zhurzh.app.nodepricelist.controller;

import com.zhurzh.app.commonjpa.enums.BranchStatus;
import com.zhurzh.app.commonutils.model.Body;
import com.zhurzh.app.commonutils.model.Branch;
import com.zhurzh.app.nodepricelist.enums.TextMessage;
import com.zhurzh.app.nodepricelist.service.PriceListService;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

@Log4j
@Component
public class PriceListController extends Branch {

    private final PriceListService priceListService;

    public PriceListController(PriceListService priceListService) {
        super(BranchStatus.PRICE_LIST);
        this.priceListService = priceListService;
    }

    @Override
    public String isActiveAndGetButtonName(@RequestBody Body body){
        return TextMessage.ACTIVATION_BUTTON.getMessage(body.getAppUser().getLanguage());
    }

    @Override
    public void execute(@RequestBody Body body){
        try {
            priceListService.manage(body.getAppUser(), body.getUpdate());
        }catch (Exception e){
            log.error(e);
        }
    }
}