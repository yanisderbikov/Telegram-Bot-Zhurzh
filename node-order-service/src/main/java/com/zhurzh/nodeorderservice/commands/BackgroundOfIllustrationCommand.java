package com.zhurzh.nodeorderservice.commands;

import com.zhurzh.commonjpa.dao.OrderDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonjpa.enums.BackgroundOfIllustration;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.model.Command;
import com.zhurzh.nodeorderservice.controller.HasUserState;
import com.zhurzh.nodeorderservice.controller.UserState;
import com.zhurzh.nodeorderservice.controller.UserStateController;
import com.zhurzh.nodeorderservice.enums.TextMessage;
import com.zhurzh.exception.CommandException;
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
public class BackgroundOfIllustrationCommand implements Command, HasUserState {
    private CommandsManager cm;
    private OrderDAO orderDAO;
//    private UserStateController us;
    private CommonCommands cc;

    @NonNull
    public static final UserState userState = UserState.BACKGROUND;

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
        if (update.hasCallbackQuery()){
            if (!update.getCallbackQuery().getData().equals(userState.getPath())) return false;
            var out = TextMessage.BACKGROUND_START.getMessage(appUser.getLanguage());
            List<List<InlineKeyboardButton>> list = new ArrayList<>();
            cc.addAllButtons(appUser, BackgroundOfIllustration.class, list);
            cm.sendAnswerEdit(appUser, update, out, list);
            return true;
        }
        return false;
    }
    private boolean endCommand(AppUser appUser, Update update){
        if (update.hasCallbackQuery()){
            var background = BackgroundOfIllustration.values()[
                    Integer.parseInt(update.getCallbackQuery().getData())];
            var out = TextMessage.BACKGROUND_END.getMessage(appUser.getLanguage());
            var order = cc.findActiveOrder(appUser);
            order.setBackgroundOfIllustration(background);
            orderDAO.save(order);
            List<InlineKeyboardButton> row = new ArrayList<>();
            cc.addButtonToNextStepAndCorrectionButton(row, appUser, userState);
            cm.sendAnswerEdit(appUser, update, out, new ArrayList<>(List.of(row)));
            return true;
        }
        return false;
    }
}
