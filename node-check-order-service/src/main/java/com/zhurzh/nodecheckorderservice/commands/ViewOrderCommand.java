package com.zhurzh.nodecheckorderservice.commands;

import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonjpa.enums.StatusZhurzh;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.commonutils.exception.CommandException;
import com.zhurzh.commonutils.model.Command;
import com.zhurzh.nodecheckorderservice.controller.HasUserState;
import com.zhurzh.nodecheckorderservice.controller.OrderCasheController;
import com.zhurzh.nodecheckorderservice.controller.UserState;
import com.zhurzh.nodecheckorderservice.service.CommonCommands;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class ViewOrderCommand implements Command, HasUserState {
    private CommandsManager cm;
    private CommonCommands cc;
    private OrderCasheController oc;
    @NonNull
    private final UserState userState = UserState.VIEW_ORDER;
    @Override
    public UserState getUserState() {
        return userState;
    }

    @Override
    public void execute(Update update) throws CommandException {
        var appUser = cm.findOrSaveAppUser(update);
        if (startCommand(appUser, update)) return;
        throw new CommandException(Thread.currentThread().getStackTrace());
    }

    @Override
    public boolean isExecuted(AppUser appUser) {
        return false;
    }

    private boolean startCommand(AppUser appUser, Update update){
        if (update.hasCallbackQuery()){
            if (!update.getCallbackQuery().getData().equals(userState.getPath())) return false;
            var order = oc.getCurrentOrder(appUser);
            var out = order.toString();
            List<InlineKeyboardButton> row = new ArrayList<>();
            cc.addButtonToNextStep(row, appUser, userState);
            if (order.getStatusZhurzh() == StatusZhurzh.UNSEEN) {
                cm.addButtonToRow(row,
                        DeleteOrderCommand.userState.getMessage(appUser.getLanguage()),
                        DeleteOrderCommand.userState.getPath());
            }
            cm.sendAnswerEdit(appUser, update, out, new ArrayList<>(List.of(row)));
            return true;
        }
        return false;
    }
}
