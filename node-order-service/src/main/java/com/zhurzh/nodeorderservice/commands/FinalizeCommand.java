package com.zhurzh.nodeorderservice.commands;

import com.zhurzh.commonjpa.dao.AppUserDAO;
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
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class FinalizeCommand implements Command, HasUserState {
    private OrderDAO orderDAO;
    private CommandsManager cm;
    private CommonCommands cc;
    private AppUserDAO appUserDAO;
    @NonNull
    public static final UserState userState = UserState.FINALIZE;

    @Override
    public UserState getUserState() {
        return userState;
    }

    @Override
    public void execute(Update update) throws CommandException {
        var appUser = cm.findOrSaveAppUser(update);
        if (startCommand(appUser, update)) return;
        if (checkCommand(appUser, update)) return;
        if (endCommand(appUser, update)) return;
        throw new CommandException(Thread.currentThread().getStackTrace());

    }
    @Override
    public boolean isExecuted(AppUser appUser) {
        return true;
    }

    private boolean startCommand(AppUser appUser, Update update) {
        if (update.hasCallbackQuery()) {
            if (!update.getCallbackQuery().getData().equals(userState.getPath())) return false;
            var order = cc.findActiveOrder(appUser);
            var out = TextMessage.FINALIZE_START.getMessage(appUser.getLanguage())
                    + order.toString();
            List<InlineKeyboardButton> row = new ArrayList<>();
            if (order.isAllFilled()) {
                cm.addButtonToRow(row,
                        TextMessage.ACCEPT.getMessage(appUser.getLanguage()),
                        TextMessage.ACCEPT.name());
            }else {
                cm.addButtonToRow(row,
                        CorrectOrderCommand.userState.getMessage(appUser.getLanguage()),
                        CorrectOrderCommand.userState.getPath());
//                cm.addButtonToRow(row,
//                        TextMessage.START_VERY_BEGIN_BUTTON.getMessage(appUser.getLanguage()),
//                        StartCommand.veryBegin);
            }
            cc.addCorrectButtonToRow(row, appUser);
            cm.sendAnswerEdit(appUser, update, out, new ArrayList<>(List.of(row)));

            return true;
        }
        return false;
    }

    private boolean checkCommand(AppUser appUser, Update update) {
        if (update.hasCallbackQuery()) {
            if (!TextMessage.ACCEPT.name().equals(update.getCallbackQuery().getData())) return false;
            var out = TextMessage.FINALIZE_AGAIN_CHECK.getMessage(appUser.getLanguage());

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

    private boolean endCommand(AppUser appUser, Update update) {
        if (update.hasCallbackQuery()) {
            var isYes = TextMessage.BUTTON_YES.name().equals(update.getCallbackQuery().getData());
            var isNo = TextMessage.BUTTON_NO.name().equals(update.getCallbackQuery().getData());

            if (!(isNo || isYes)) return false;

            if (isYes) {
                var needAddMes = appUser.getAdditionalMessenger() == null;
                var out = TextMessage.FINALIZE_FINAL_ORDER.getMessage(appUser.getLanguage());
                var order = cc.findActiveOrder(appUser);
                order.setIsFinished(true);
                orderDAO.save(order);
                List<List<InlineKeyboardButton>> lists = new ArrayList<>();
                if (needAddMes){
                    out += TextMessage.FINALIZE_ADDITIONAL_MESSANGER.getMessage(appUser.getLanguage());
                    cm.addButtonToList(lists,
                            AdditionalMessengerCommand.userState.getMessage(appUser.getLanguage()),
                            AdditionalMessengerCommand.userState.getPath());
                }
                cm.addButtonToList(lists, TextMessage.TO_MAIN_MENU.getMessage(appUser.getLanguage()),
                        "/menu");
                cm.sendAnswerEdit(appUser, update, out, lists);
            }else {
                var out = TextMessage.FINALIZE_AFTER_NO.getMessage(appUser.getLanguage());
                List<InlineKeyboardButton> row = new ArrayList<>();
                cc.addButtonToNextStepAndCorrectionButton(row, appUser, userState);
                cm.sendAnswerEdit(appUser, update, out, new ArrayList<>(List.of(row)));
            }
            return true;
        }
        return false;
    }


}
