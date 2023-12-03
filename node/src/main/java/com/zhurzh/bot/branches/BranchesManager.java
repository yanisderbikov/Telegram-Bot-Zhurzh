package com.zhurzh.bot.branches;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import com.zhurzh.bot.branches.order.CheckOrderManager;
import com.zhurzh.bot.branches.order.OrderManager;

@Component
@AllArgsConstructor
public class BranchesManager {
    private CheckOrderManager checkOrderManager;
    private OrderManager orderManager;
    public boolean checkServices(){
        return checkOrderManager.isActive() && orderManager.isActive();
    }
}
