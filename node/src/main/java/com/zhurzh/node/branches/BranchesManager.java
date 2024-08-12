package com.zhurzh.node.branches;

import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonutils.model.Body;
import com.zhurzh.commonutils.model.Branch;
import com.zhurzh.node.branches.main.MainMenu;
import com.zhurzh.node.service.CheckLastMessage;
import com.zhurzh.node.service.ConnectionAppUser;
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


    @Override
    public void consume(Update update) {
        var appUser = connectionAppUser.findOrSaveAppUser(update);
        if (checkLastMessage.checkIsLastMessageAndSave(update)) return;
        findCurrentBranch(appUser, update).execute(new Body(appUser, update));
    }

    private Branch findCurrentBranch(AppUser appUser, Update update) {
        for (Branch branch : branches) {
            if (branch.isCurrentBranch(update, appUser.getBranchStatus())) {
                return branch;
            }
        }
        return mainMenu;
    }
}
