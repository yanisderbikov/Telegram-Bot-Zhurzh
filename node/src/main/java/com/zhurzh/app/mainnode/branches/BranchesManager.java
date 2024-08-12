package com.zhurzh.app.mainnode.branches;

import com.zhurzh.app.commonjpa.dao.AppUserDAO;
import com.zhurzh.app.commonjpa.entity.AppUser;
import com.zhurzh.app.commonutils.model.Body;
import com.zhurzh.app.commonutils.model.Branch;
import com.zhurzh.app.mainnode.branches.main.MainMenu;
import com.zhurzh.app.mainnode.service.CheckLastMessage;
import com.zhurzh.app.mainnode.service.ConnectionAppUser;
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


    @Override
    public void consume(Update update) {
        var appUser = connectionAppUser.findOrSaveAppUser(update);
        if (checkLastMessage.checkIsLastMessageAndSave(update)) return;
        findCurrentBranch(appUser, update).execute(new Body(appUser, update));
    }

    private Branch findCurrentBranch(AppUser appUser, Update update) {
        for (Branch branch : branches) {
            if (branch.isCurrentBranch(update, appUser.getBranchStatus())) {
                appUser.setBranchStatus(branch.getCurrentBranchStatus());
                appUserDAO.save(appUser);
                return branch;
            }
        }
        return mainMenu;
    }
}
