package com.zhurzh.nodeorderservice.commands;

import com.zhurzh.commonjpa.dao.OrderDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.commonutils.exception.CommandException;
import com.zhurzh.commonutils.model.Command;
import com.zhurzh.nodeorderservice.controller.HasUserState;
import com.zhurzh.nodeorderservice.controller.UserState;
import com.zhurzh.nodeorderservice.ehcache.MyCacheManager;
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
public class CommentToArtCommand implements Command, HasUserState {
    private CommandsManager cm;
    private OrderDAO orderDAO;
    private CommonCommands cc;

    private MyCacheManager cacheManager;

    @NonNull
    public static final UserState userState = UserState.COMMENT_TO_ART;

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
        return order.getCommentToArt() != null;
    }
    private boolean startCommand(AppUser appUser, Update update){
        if (update.hasCallbackQuery()){
            if (!update.getCallbackQuery().getData().equals(userState.getPath())) return false;
            var out = TextMessage.COMMENT_TO_ART_START.getMessage(appUser.getLanguage());
            List<List<InlineKeyboardButton>> list = new ArrayList<>();
            cm.addButtonToList(list,
                    TextMessage.BUTTON_SKIP.getMessage(appUser.getLanguage()),
                    TextMessage.BUTTON_SKIP.name());
            cm.sendAnswerEdit(appUser, update, out, list);
            return true;
        }
        return false;
    }

    private boolean endCommand(AppUser appUser, Update update) throws CommandException {
        if (update.hasMessage() && update.getMessage().getMediaGroupId() != null
                && cacheManager.checkAndAdd(update.getMessage().getMediaGroupId())){
            return true;
        }
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals(TextMessage.BUTTON_SKIP.name())){
            saveCommentAndSendMessage(appUser, update, "");
            return true;
        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            var input = update.getMessage().getText();
            saveCommentAndSendMessage(appUser, update, input);
            return true;
        }

        // if photos or smth else
        var out = TextMessage.COMMENT_TO_ART_ERROR.getMessage(appUser.getLanguage());
        cm.sendAnswerEdit(appUser, update, out);
        return true;
    }

    private void saveCommentAndSendMessage(AppUser appUser, Update update, String input) throws CommandException {
        var order = cc.findActiveOrder(appUser);
        order.setCommentToArt(input);
        orderDAO.save(order);
        if (!input.isEmpty()) {
            var out = TextMessage.COMMENT_TO_ART_END.getMessage(appUser.getLanguage());
            List<InlineKeyboardButton> row = new ArrayList<>();
            cc.addButtonToNextStepAndCorrectionButton(row, appUser, userState);
            cm.sendAnswerEdit(appUser, update, out, new ArrayList<>(List.of(row)));
        }else {
            cc.getNextCommandAndExecute(appUser, update);
        }
    }

}
