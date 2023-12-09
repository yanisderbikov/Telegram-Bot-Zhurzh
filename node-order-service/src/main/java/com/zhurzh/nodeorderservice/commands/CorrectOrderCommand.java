package com.zhurzh.nodeorderservice.commands;

import com.zhurzh.commonjpa.dao.OrderDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.exception.CommandException;
import com.zhurzh.model.Command;
import com.zhurzh.nodeorderservice.controller.HasUserState;
import com.zhurzh.nodeorderservice.controller.UserState;
import com.zhurzh.nodeorderservice.enums.TextMessage;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class CorrectOrderCommand implements Command, HasUserState {
    private CommandsManager cm;

    @NonNull
    public static final UserState userState = UserState.CORRECT_ORDER;

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
    private boolean startCommand(AppUser appUser, Update update){
        if (update.hasCallbackQuery()) {
            if (!update.getCallbackQuery().getData().equals(userState.getPath())) return false;
            var out = TextMessage.CORRECT_ORDER.getMessage(appUser.getLanguage());
            List<List<InlineKeyboardButton>> lists = new ArrayList<>();
            cm.addButtonToList(lists,
                    CountPersonsCommand.userState.getMessage(appUser.getLanguage()),
                    CountPersonsCommand.userState.getPath());

            cm.addButtonToList(lists,
                    ReferencesCommand.userState.getMessage(appUser.getLanguage()),
                    ReferencesCommand.userState.getPath());

            cm.addButtonToList(lists,
                    FormatIllustrationCommand.userState.getMessage(appUser.getLanguage()),
                    FormatIllustrationCommand.userState.getPath());

            cm.addButtonToList(lists,
                    DetalizationOfIllustrationCommand.userState.getMessage(appUser.getLanguage()),
                    DetalizationOfIllustrationCommand.userState.getPath());

            cm.addButtonToList(lists,
                    BackgroundOfIllustrationCommand.userState.getMessage(appUser.getLanguage()),
                    BackgroundOfIllustrationCommand.userState.getPath());

            cm.addButtonToList(lists,
                    NameCreationCommand.userState.getMessage(appUser.getLanguage()),
                    NameCreationCommand.userState.getPath());

            cm.addButtonToList(lists,
                    PriceCommand.userState.getMessage(appUser.getLanguage()),
                    PriceCommand.userState.getPath());

            cm.addButtonToList(lists,
                    CommentToArtCommand.userState.getMessage(appUser.getLanguage()),
                    CommentToArtCommand.userState.getPath());

            // выход
            cm.addButtonToList(lists,
                    FinalizeCommand.userState.getMessage(appUser.getLanguage()),
                    FinalizeCommand.userState.getPath());

            cm.sendAnswerEdit(appUser, update, out, lists);
            return true;
        }
        return false;
    }

}
