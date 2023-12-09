package com.zhurzh.nodeorderservice.commands;

import com.zhurzh.commonjpa.dao.OrderDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonjpa.enums.DetalizationOfIllustration;
import com.zhurzh.commonjpa.enums.FormatOfIllustration;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.exception.CommandException;
import com.zhurzh.model.Command;
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

@AllArgsConstructor
@Component
public class FormatIllustrationCommand implements Command, HasUserState {

    private CommandsManager cm;
    private CommonCommands cc;
    private OrderDAO orderDAO;

    @NonNull
    public static final UserState userState = UserState.FORMAT_ILLUSTRATION;

    @Override
    public UserState getUserState() {
        return userState;
    }

    @Override
    public void execute(Update update) throws CommandException {
        var appUser = cm.findOrSaveAppUser(update);
        if (startCommand(appUser, update)) return;
        if (endCommand(appUser, update)) return;
        throw new CommandException(Thread.currentThread().getStackTrace());

    }

    private boolean startCommand(AppUser appUser, Update update){
        if (update.hasCallbackQuery()) {
            var input = update.getCallbackQuery().getData();
            if (!input.equals(userState.getPath())) return false;
            var out = TextMessage.START.getMessage(appUser.getLanguage());
            List<List<InlineKeyboardButton>> lists = new ArrayList<>();
            cc.addAllButtons(appUser, FormatOfIllustration.class, lists);
            cm.sendAnswerEdit(appUser, update, out, lists);
            return true;
        }
        return false;

    }
    private boolean endCommand(AppUser appUser, Update update){
        if (update.hasCallbackQuery()){
            var input = Integer.parseInt(update.getCallbackQuery().getData());
            var format = FormatOfIllustration.values()[input];
            var out = TextMessage.FORMAT_ILLUSTRATION_END.getMessage(appUser.getLanguage())
                    + format.getMessage(appUser.getLanguage());
            var order = cc.findActiveOrder(appUser);
            order.setFormatOfIllustration(format);
            orderDAO.save(order);
            List<List<InlineKeyboardButton>> lists = new ArrayList<>();
            List<InlineKeyboardButton> row = new ArrayList<>();
            cc.addButtonToNextStepAndCorrectionButton(row, appUser, userState);
            lists.add(row);
            cm.sendAnswerEdit(appUser, update, out, lists);
            return true;
        }
        return false;
    }
}
