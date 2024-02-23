package com.zhurzh.nodeorderservice.commands;

import com.zhurzh.commonjpa.dao.AppUserDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.commonutils.exception.CommandException;
import com.zhurzh.commonutils.model.Command;
import com.zhurzh.nodeorderservice.controller.HasUserState;
import com.zhurzh.nodeorderservice.controller.UserState;
import com.zhurzh.nodeorderservice.enums.TextMessage;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Log4j
@AllArgsConstructor
public class AdditionalMessengerCommand implements Command, HasUserState {
    private CommandsManager cm;
    private AppUserDAO appUserDAO;
    @NonNull
    private Map<AppUser, String> mapCashe = new HashMap<>();
    @NonNull
    public static final UserState userState = UserState.ADDITIONAL_MESSANGER;

    @Override
    public UserState getUserState() {
        return userState;
    }

    @Override
    public void execute(AppUser appUser, Update update) throws CommandException {
        if (startCommand(appUser, update)) return;
        if (doubleCheck(appUser, update)) return;
        if (endCommand(appUser, update)) return;
        throw new CommandException(Thread.currentThread().getStackTrace());

    }
    @Override
    public boolean isExecuted(AppUser appUser) {
        return true;
    }

    private boolean startCommand(AppUser appUser, Update update){
        if (update.hasCallbackQuery()){
            if (!update.getCallbackQuery().getData().equals(userState.getPath())) return false;
            var out = TextMessage.ADDITIONAL_MESSANGER_ADD.getMessage(appUser.getLanguage());
            cm.sendAnswerEdit(appUser, update, out);
            return true;
        }
        return false;
    }

    private boolean doubleCheck(AppUser appUser, Update update){
        if (update.hasMessage() && update.getMessage().hasText()){
            var input = update.getMessage().getText();
            mapCashe.put(appUser, input);
            var out = TextMessage.ADDITIONAL_MESSANGER_IS_CORRECT.getMessage(appUser.getLanguage())
                    + "\n\n" + input;
            List<InlineKeyboardButton> row = new ArrayList<>();

            cm.addButtonToRow(row,
                    TextMessage.BUTTON_YES.getMessage(appUser.getLanguage()),
                    TextMessage.BUTTON_YES.name());

            cm.addButtonToRow(row,
                    TextMessage.BUTTON_NO.getMessage(appUser.getLanguage()),
                    TextMessage.BUTTON_NO.name());

            cm.sendAnswerEdit(appUser, update, out, new ArrayList<>(List.of(row)));
            return true;
        }
        return false;
    }

    private boolean endCommand(AppUser appUser, Update update){
        if (update.hasCallbackQuery()){
            var isYes = TextMessage.BUTTON_YES.name().equals(update.getCallbackQuery().getData());
            var isNo = TextMessage.BUTTON_NO.name().equals(update.getCallbackQuery().getData());
            if (!(isNo || isYes)) return false;
            var out = "";
            List<List<InlineKeyboardButton>> lists = new ArrayList<>();
            if (isYes) {
                appUser.setAdditionalMessenger(mapCashe.get(appUser));
                appUserDAO.save(appUser);
                out = TextMessage.ADDITIONAL_MESSANGER_SAVE.getMessage(appUser.getLanguage());
                cm.addButtonToMainMenu(lists, appUser);
                cm.sendAnswerEdit(appUser, update, out, lists);
            }else {
                out = TextMessage.ADDITIONAL_MESSANGER_ADD.getMessage(appUser.getLanguage());
                cm.sendAnswerEdit(appUser, update, out);
            }
            return true;
        }
        return false;
    }


}
