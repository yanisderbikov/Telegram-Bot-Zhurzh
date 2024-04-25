package com.zhurzh.nodecheckorderservice.commands;

import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonjpa.entity.Order;
import com.zhurzh.commonjpa.enums.StatusZhurzh;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.commonutils.exception.CommandException;
import com.zhurzh.commonutils.model.Command;
import com.zhurzh.nodecheckorderservice.controller.HasUserState;
import com.zhurzh.nodecheckorderservice.controller.OrderCasheController;
import com.zhurzh.nodecheckorderservice.controller.UserState;
import com.zhurzh.nodecheckorderservice.enums.TextMessage;
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
    private OrderCasheController orderCasheController;
    @NonNull
    private final UserState userState = UserState.VIEW_ORDER;

    @Override
    public UserState getUserState() {
        return userState;
    }

    @Override
    @Deprecated
    public void execute(AppUser appUser, Update update) throws CommandException {
        throw new CommandException(Thread.currentThread().getStackTrace());
    }

    @Override
    public boolean isExecuted(AppUser appUser) {
        return false;
    }

    public void showOrder(AppUser appUser, Update update, Order order) {
        var out = order.toString();
        List<InlineKeyboardButton> row = new ArrayList<>();
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        cm.addButtonToRow(row,
                TextMessage.BUTTON_BACK_TO_LIST.getMessage(appUser.getLanguage()),
                ChooseOrderCommand.userState.getPath());
        if (order.getStatusZhurzh() == StatusZhurzh.UNSEEN || order.getStatusZhurzh() == StatusZhurzh.SEEN) {
            orderCasheController.setOrder(appUser, order);
            cm.addButtonToRow(row,
                    DeleteOrderCommand.userState.getMessage(appUser.getLanguage()),
                    DeleteOrderCommand.userState.getPath());
        }
        lists.add(row);
        cm.addButtonToMainMenu(lists, appUser);
        cm.sendAnswerEdit(appUser, update, out, lists);
    }
}
