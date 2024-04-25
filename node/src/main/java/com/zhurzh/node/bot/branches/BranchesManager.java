package com.zhurzh.node.bot.branches;

import com.zhurzh.commonjpa.dao.AppUserDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonjpa.enums.BranchStatus;
import com.zhurzh.commonutils.model.Body;
import com.zhurzh.commonutils.model.Branches;
import com.zhurzh.node.bot.branches.faq.FAQManager;
import com.zhurzh.node.bot.branches.mainmenu.MainMenu;
import com.zhurzh.node.bot.branches.pricelist.PriceListManager;
import com.zhurzh.node.bot.branches.searem.SeaRemBranch;
import com.zhurzh.node.bot.branches.start.StartManager;
import com.zhurzh.node.service.CheckLastMessage;
import com.zhurzh.node.service.ConnectionAppUser;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import com.zhurzh.node.bot.branches.order.CheckOrderManager;
import com.zhurzh.node.bot.branches.order.OrderManager;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@AllArgsConstructor
@Log4j
public class BranchesManager implements BranchesManagerInterface{

    private AppUserDAO appUserDAO;
    private CheckOrderManager checkOrderManager;
    private OrderManager orderManager;
    private StartManager startManager;
    private MainMenu mainMenu;
    private PriceListManager priceListManager;
    private FAQManager faqManager;
    private SeaRemBranch seaRemBranch;
    private ConnectionAppUser connectionAppUser;
    private CheckLastMessage checkLastMessage;

    @Override
    public void consume(Update update) {
        var appUser = connectionAppUser.findOrSaveAppUser(update);
        if (checkLastMessage.checkIsLastMessageAndSave(update)) return;
        findCurrentBranch(appUser, update).execute(new Body(appUser, update));
    }


    private Branches findCurrentBranch(AppUser appUser, Update update){
        Branches branches;

        if (isStartBranch(update, appUser)) return startManager;
        if (isMenuBranch(update, appUser)) return mainMenu;
        if (isCheckOrderBranch(update, appUser)) return checkOrderManager;
        if (isOrderBranch(update, appUser)) return orderManager;
        if (isPriceListBranch(update, appUser)) return priceListManager;
        if (isFaqBranch(update, appUser)) return faqManager;
        if (isSeaRemBranch(update, appUser)) return seaRemBranch;
        switch (appUser.getBranchStatus()){
            case START -> branches = startManager;
            case ORDER -> branches = orderManager;
            case PRICE_LIST -> branches = priceListManager;
            case CHECK_ORDER -> branches = checkOrderManager;
            case FAQ -> branches = faqManager;
            case SEA_REM -> branches = seaRemBranch;
            default -> branches = mainMenu;
        }
        return branches;
    }

    private boolean isSeaRemBranch(Update update, AppUser appUser) {
        var isTrue =  update.hasCallbackQuery() &&
                update.getCallbackQuery().getData().equals(seaRemBranch.getCallbackPath());
        if (isTrue) {
            appUser.setBranchStatus(BranchStatus.SEA_REM);
            appUserDAO.save(appUser);
            return true;
        }
        return false;
    }

    private boolean isFaqBranch(Update update, AppUser appUser) {
        var isTrue =  update.hasCallbackQuery() &&
                update.getCallbackQuery().getData().equals(faqManager.getCallbackPath());
        if (isTrue) {
            appUser.setBranchStatus(BranchStatus.FAQ);
            appUserDAO.save(appUser);
            return true;
        }
        return false;
    }

    private boolean isPriceListBranch(Update update, AppUser appUser) {
        var isTrue =  update.hasCallbackQuery() &&
                update.getCallbackQuery().getData().equals(priceListManager.getCallbackPath());
        if (isTrue) {
            appUser.setBranchStatus(BranchStatus.PRICE_LIST);
            appUserDAO.save(appUser);
            return true;
        }
        return false;
    }
    private boolean isCheckOrderBranch(Update update, AppUser appUser) {
        var isTrue =  update.hasCallbackQuery() &&
                update.getCallbackQuery().getData().equals(checkOrderManager.getCallbackPath());
        if (isTrue) {
            appUser.setBranchStatus(BranchStatus.CHECK_ORDER);
            appUserDAO.save(appUser);
            return true;
        }
        return false;
    }

    private boolean isOrderBranch(Update update, AppUser appUser) {
        var isTrue = update.hasCallbackQuery() &&
                update.getCallbackQuery().getData().equals(orderManager.getCallbackPath());
        if (isTrue) {
            appUser.setBranchStatus(BranchStatus.ORDER);
            appUserDAO.save(appUser);
            return true;
        }
        return false;
    }
    private boolean isStartBranch(Update update, AppUser appUser){
        if (appUser.getBranchStatus() == null || (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("/start"))
                || (update.hasMessage() && update.getMessage().hasText() && (update.getMessage().getText().equals("/start") || update.getMessage().getText().equals("/switch_language")))){
            appUser.setBranchStatus(BranchStatus.START);
            appUserDAO.save(appUser);
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isMenuBranch(Update update, AppUser appUser){
        if (appUser.getBranchStatus() == null || (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("/menu"))
                || (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals("/menu"))
        ){
            appUser.setBranchStatus(BranchStatus.MENU);
            appUserDAO.save(appUser);
            return true;
        }
        else {
            return false;
        }
    }
}
