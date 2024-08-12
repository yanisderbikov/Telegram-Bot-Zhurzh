package com.zhurzh.commonutils.model;

import com.zhurzh.commonjpa.enums.BranchStatus;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.Update;

@AllArgsConstructor
public abstract class Branch implements BranchesInterface {

    @NonNull private String path;
    @NonNull private BranchStatus currentBranchStatus;

    public boolean isCurrentBranch(Update update, BranchStatus branchStatus) {
        if (byBranchStatus(branchStatus)) return true;
        if (byMessage(update)) return true;
        return false;
    }

    private boolean byMessage(Update update) {
        if (update.hasMessage()) {
            var message = update.getMessage();
            return message.getText().startsWith(path);
        } else if (update.hasCallbackQuery()) {
            var callback = update.getCallbackQuery();
            return callback.getData().startsWith(path);
        }
        return false;
    }

    private boolean byBranchStatus(BranchStatus branchStatus) {
        return currentBranchStatus == branchStatus;
    }
}
