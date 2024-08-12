package com.zhurzh.app.commonutils.model;

import com.zhurzh.app.commonjpa.enums.BranchStatus;
import lombok.Getter;
import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
public abstract class Branch implements BranchesInterface {

    private final String path;
    private final BranchStatus currentBranchStatus;

    public Branch(@NonNull BranchStatus currentBranchStatus) {
        this.path = currentBranchStatus.getPath();
        this.currentBranchStatus = currentBranchStatus;
    }

    /**
     *
     * @param update текущее сообщение
     * @param branchStatus текущий статус в {@link com.zhurzh.app.commonjpa.entity.AppUser}
     * @return true если это сообщение из этого, если нет, то false
     */
    public boolean isCurrentBranch(Update update, BranchStatus branchStatus) {
        return byMessage(update) || byBranchStatus(branchStatus);
    }

    private boolean byMessage(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
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
