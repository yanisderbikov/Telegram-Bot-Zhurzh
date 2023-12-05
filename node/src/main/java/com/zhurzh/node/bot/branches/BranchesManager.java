package com.zhurzh.node.bot.branches;


import com.zhurzh.node.bot.branches.start.StartManager;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import com.zhurzh.node.bot.branches.order.CheckOrderManager;
import com.zhurzh.node.bot.branches.order.OrderManager;

@Component
@AllArgsConstructor
@Log4j
public class BranchesManager {
    private CheckOrderManager checkOrderManager;
    private OrderManager orderManager;
    private StartManager startManager;
    public boolean checkServices(){

        log.debug("StartManager : " + startManager.isActive().getBody());
        log.debug("OrderManager : " + orderManager.isActive().getBody());
        log.debug("CheckOrderManager : " + checkOrderManager.isActive().getBody());

        return isActive(startManager.isActive()) && isActive(orderManager.isActive()) && isActive(checkOrderManager.isActive());

    }
    private boolean isActive(ResponseEntity<String> responseEntity){
        return responseEntity.getStatusCode().value() >= 200 && responseEntity.getStatusCode().value() < 300;
    }


}
