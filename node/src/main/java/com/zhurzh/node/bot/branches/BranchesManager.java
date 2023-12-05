package com.zhurzh.node.bot.branches;


import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.model.Branches;
import com.zhurzh.node.bot.branches.mainmenu.MainMenu;
import com.zhurzh.node.bot.branches.start.StartManager;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import com.zhurzh.node.bot.branches.order.CheckOrderManager;
import com.zhurzh.node.bot.branches.order.OrderManager;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@AllArgsConstructor
@Log4j
public class BranchesManager {
    private CheckOrderManager checkOrderManager;
    private OrderManager orderManager;
    private StartManager startManager;
    private MainMenu mainMenu;
    private CommandsManager commandsManager;

    public void consumeText(Update update){
        findCurrentBranch(update).manageText(update);
    }
    public void consumeCallBack(Update update){
        findCurrentBranch(update).manageCallBack(update);
    }

    private Branches findCurrentBranch(Update update){
        Branches branches;
        AppUser appUser = commandsManager.findOrSaveAppUser(update);
        switch (appUser.getBranchStatus()){
            case ORDER -> branches = orderManager;
            case MENU -> branches = mainMenu;
            case CHECK_ORDER -> branches = checkOrderManager;
            default -> branches = startManager;
        }
        return branches;
    }
}
