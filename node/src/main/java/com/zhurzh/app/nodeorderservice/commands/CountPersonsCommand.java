package com.zhurzh.app.nodeorderservice.commands;

import com.zhurzh.app.nodeorderservice.controller.UserState;
import com.zhurzh.app.nodeorderservice.enums.TextMessage;
import com.zhurzh.app.nodeorderservice.service.CommonCommands;
import com.zhurzh.app.commonjpa.dao.OrderDAO;
import com.zhurzh.app.commonjpa.entity.AppUser;
import com.zhurzh.app.commonjpa.entity.Order;
import com.zhurzh.app.commonjpa.enums.CountOfPersons;
import com.zhurzh.app.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.app.commonutils.exception.CommandException;
import com.zhurzh.app.commonutils.model.Command;
import com.zhurzh.app.nodeorderservice.controller.HasUserState;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@Log4j
public class CountPersonsCommand implements Command, HasUserState {
    private CommandsManager cm;
    private OrderDAO orderDAO;
    private CommonCommands cc;

    @NonNull
    public static final UserState userState = UserState.COUNT_PERSONS;

    @Override
    public UserState getUserState() {
        return userState;
    }

    @Override
    public void execute(AppUser appUser, Update update) throws CommandException {
        if (startCommand(appUser, update)) return;
        if (endCommand(update, appUser)) return;
        throw new CommandException(Thread.currentThread().getStackTrace());

    }
    @Override
    public boolean isExecuted(AppUser appUser) {
        var order = cc.findActiveOrder(appUser);
        return order.getCountOfPersons() != null;
    }

    private boolean startCommand(AppUser appUser, Update update) {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals(userState.getPath())) {
            var len = appUser.getLanguage();
            var out = TextMessage.COUNT_OF_PERSONS_START.getMessage(len);
            List<List<InlineKeyboardButton>> lists = new ArrayList<>();
            cc.addAllButtons(appUser, CountOfPersons.class, lists);
            cm.sendAnswerEdit(appUser, update, out, lists);
            return true;
        }
        return false;
    }

    private boolean endCommand(Update update, AppUser appUser) throws CommandException {
        if (update.hasCallbackQuery()) {
            var ordinal = Integer.parseInt(update.getCallbackQuery().getData());
            var c = CountOfPersons.values()[ordinal];
            Order order = cc.findActiveOrder(appUser);
            order.setCountOfPersons(c);
            orderDAO.save(order);

            cc.getNextCommandAndExecute(appUser, update);
            return true;
        }
        return false;
    }
}
