package com.zhurzh.nodeorderservice.commands;

import com.zhurzh.commonjpa.dao.OrderDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.commonutils.exception.CommandException;
import com.zhurzh.commonutils.model.Command;
import com.zhurzh.nodeorderservice.controller.HasUserState;
import com.zhurzh.nodeorderservice.controller.UserState;
import com.zhurzh.nodeorderservice.enums.TextMessage;
import com.zhurzh.nodeorderservice.service.CommonCommands;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Log4j
@Component
public class NameCreationCommand implements Command, HasUserState {
    private CommandsManager cm;
    private CommonCommands cc;
    private OrderDAO orderDAO;
    @NonNull
    public static final UserState userState = UserState.NAME;

    @Override
    public UserState getUserState() {
        return userState;
    }

    @Override
    public void execute(AppUser appUser, Update update) throws CommandException {
        if (startCommand(appUser, update)) return;
        if (endCommand(appUser, update)) return;
        throw new CommandException(Thread.currentThread().getStackTrace());

    }
    @Override
    public boolean isExecuted(AppUser appUser) {
        var order = cc.findActiveOrder(appUser);
        return order.getName() != null;
    }

    private boolean startCommand(AppUser appUser, Update update){
        if (update.hasCallbackQuery()){
            if (!update.getCallbackQuery().getData().equals(userState.getPath())) return false;
            var out = TextMessage.NAME_START.getMessage(appUser.getLanguage());
            cm.sendAnswerEdit(appUser, update, out);
            return true;
        }
        return false;
    }

    private boolean endCommand(AppUser appUser, Update update){
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                var input = update.getMessage().getText();
                var order = cc.findActiveOrder(appUser);
                order.setName(input);
                orderDAO.save(order);
                var out = TextMessage.NAME_END.getMessage(appUser.getLanguage());
                List<InlineKeyboardButton> row = new ArrayList<>();
                cc.addButtonToNextStepAndCorrectionButton(row, appUser, userState);
                cm.sendAnswerEdit(appUser, update, out, new ArrayList<>(List.of(row)));
                return true;
            }
        }catch (Exception e){
            log.error(e);
        }
        return false;
    }
}
