package com.zhurzh.nodeorderservice.commands;

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
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class CorrectOrderCommand implements Command, HasUserState {
    private CommandsManager cm;
    private CommonCommands cc;

    @NonNull
    public static final UserState userState = UserState.CORRECT_ORDER;

    @Override
    public UserState getUserState() {
        return userState;
    }

    @Override
    public void execute(AppUser appUser, Update update) throws CommandException {
        if (startCommand(appUser, update)) return;
        throw new CommandException(Thread.currentThread().getStackTrace());

    }

    @Override
    public boolean isExecuted(AppUser appUser) {
        return true;
    }

    private boolean startCommand(AppUser appUser, Update update) {
        if (update.hasCallbackQuery()) {
            if (!update.getCallbackQuery().getData().equals(userState.getPath())) return false;
            var out = TextMessage.CORRECT_ORDER.getMessage(appUser.getLanguage());
            var lists = addButtonsToAll(appUser);
            cm.sendAnswerEdit(appUser, update, out, lists);
            return true;
        }
        return false;
    }

    private List<List<InlineKeyboardButton>> addButtonsToAll(AppUser appUser) {
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        var order = cc.findActiveOrder(appUser);
        var filled = order.isAllFilledExceptPrice();
        if (!filled) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            cc.addButtonToNextStepAndCorrectionButton(row, appUser, userState);
            return new ArrayList<>(List.of(row));
        }

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
                CommentToArtCommand.userState.getMessage(appUser.getLanguage()),
                CommentToArtCommand.userState.getPath());

        cm.addButtonToList(lists,
                DeadLineCommand.userState.getMessage(appUser.getLanguage()),
                DeadLineCommand.userState.getPath());

        cm.addButtonToList(lists,
                PriceCommand.userState.getMessage(appUser.getLanguage()),
                PriceCommand.userState.getPath());

        List<InlineKeyboardButton> row = new ArrayList<>();

        cm.addButtonToRow(row,
                FinalizeCommand.userState.getMessage(appUser.getLanguage()),
                FinalizeCommand.userState.getPath());

        cm.addButtonToRow(row,
                TextMessage.START_VERY_BEGIN_BUTTON.getMessage(appUser.getLanguage()),
                StartCommand.veryBegin);
        lists.add(row);

        return lists;
    }

}
