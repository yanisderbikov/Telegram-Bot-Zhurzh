package com.zhurzh.nodeorderservice.commands;

import com.zhurzh.commonjpa.dao.OrderDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonjpa.enums.CountOfPersons;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.exception.CommandException;
import com.zhurzh.model.Command;
import com.zhurzh.nodeorderservice.controller.HasUserState;
import com.zhurzh.nodeorderservice.controller.UserState;
import com.zhurzh.nodeorderservice.controller.UserStateController;
import com.zhurzh.nodeorderservice.enums.TextMessage;
import com.zhurzh.nodeorderservice.service.CommonCommands;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
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
    public void execute(Update update) throws CommandException {
        var appUser = cm.findOrSaveAppUser(update);
        if (startCommand(appUser, update)) return;
        if (endCommand(update, appUser)) return;
        throw new CommandException(Thread.currentThread().getStackTrace());

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

    private boolean endCommand(Update update, AppUser appUser) {
        if (update.hasCallbackQuery()) {
            var ordinal = Integer.parseInt(update.getCallbackQuery().getData());
            var c = CountOfPersons.values()[ordinal];
            var order = cc.findActiveOrder(appUser);
            order.setCountOfPersons(c);
            orderDAO.save(order);
            List<InlineKeyboardButton> row = new ArrayList<>();
            cc.addButtonToNextStepAndCorrectionButton(row, appUser, userState);
            List<List<InlineKeyboardButton>> lists = new ArrayList<>();
            lists.add(row);
            var out = TextMessage.COUNT_OF_PERSONS_END.getMessage(appUser.getLanguage());
            cm.sendAnswerEdit(appUser, update, out, lists);
            return true;
        }
        return false;
    }
}
