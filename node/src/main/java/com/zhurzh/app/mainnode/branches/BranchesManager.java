package com.zhurzh.app.mainnode.branches;

import com.zhurzh.app.commonjpa.dao.AppUserDAO;
import com.zhurzh.app.commonjpa.entity.AppUser;
import com.zhurzh.app.commonjpa.enums.BranchStatus;
import com.zhurzh.app.commonutils.model.Body;
import com.zhurzh.app.commonutils.model.Branch;
import com.zhurzh.app.mainnode.branches.main.MainMenu;
import com.zhurzh.app.mainnode.service.CheckLastMessage;
import com.zhurzh.app.mainnode.service.ConnectionAppUser;
import com.zhurzh.app.nodestartservice.controller.StartController;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@Log4j
@AllArgsConstructor
public class BranchesManager implements BranchesManagerInterface {

    private final List<Branch> branches;
    private CheckLastMessage checkLastMessage;
    private ConnectionAppUser connectionAppUser;
    private MainMenu mainMenu;
    private AppUserDAO appUserDAO;
    private StartController startController;


    @Override
    public void consume(Update update) {
        var appUser = connectionAppUser.findOrSaveAppUser(update);
        if (checkLastMessage.checkIsLastMessageAndSave(update)) return;
        findCurrentBranch(appUser, update).execute(new Body(appUser, update));
    }

    private Branch findCurrentBranch(AppUser appUser, Update update) {

        if (update.hasMessage() && update.getMessage().hasText()
                && "/switch_language".equals(update.getMessage().getText())) {
            saveStatus(BranchStatus.START, appUser);
            return startController;
        }

        var branch = findByPath(update);
        if (branch != null) {
            saveStatus(branch.getCurrentBranchStatus(), appUser);
            return branch;
        }

        var branchStatus = appUser.getBranchStatus();
        for (Branch b : branches) {
            if (b.isCurrentBranch(update, branchStatus)) {
                saveStatus(b.getCurrentBranchStatus(), appUser);
                return b;
            }
        }
        return mainMenu;
    }

    private Branch findByPath(Update update) {
        if (update.hasMessage() && update.getMessage().hasText() || update.hasCallbackQuery()) {
            var path = update.hasCallbackQuery() ? update.getCallbackQuery().getData() : update.getMessage().getText();
            for (Branch branch : branches) {
                if (branch.getPath().equals(path)) {
                    return branch;
                }
            }
        }
        return null;
    }

    private void saveStatus(BranchStatus branchStatus, AppUser appUser) {
        appUser.setBranchStatus(branchStatus);
        appUserDAO.save(appUser);
    }
}
