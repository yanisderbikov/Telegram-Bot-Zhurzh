package com.zhurzh.app.nodeorderservice.commands;

import com.zhurzh.app.nodeorderservice.enums.TextMessage;
import com.zhurzh.app.nodeorderservice.service.CommonCommands;
import com.zhurzh.app.commonjpa.dao.OrderDAO;
import com.zhurzh.app.commonjpa.entity.AppUser;
import com.zhurzh.app.commonjpa.entity.Order;
import com.zhurzh.app.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.app.commonutils.exception.CommandException;
import com.zhurzh.app.commonutils.model.Command;
import com.zhurzh.app.nodeorderservice.controller.HasUserState;
import com.zhurzh.app.nodeorderservice.controller.UserState;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Log4j
@Component
public class StartCommand implements Command, HasUserState {
    @Autowired
    private CommandsManager cm;
    @Autowired
    private CommonCommands cc;
    @Autowired
    private OrderDAO orderDAO;

    public static final UserState userState = UserState.START;

    public static final String veryBegin = "/very_begin";

    @Value("${image.path.unfinished.order}")
    private String unfinishedImage;
    @Value("${image.path.initial.order.ru}")
    private String initialImageRu;
    @Value("${image.path.initial.order.eng}")
    private String initialImageEng;

    @Override
    public UserState getUserState() {
        return userState;
    }

    @Override
    public void execute(AppUser appUser, Update update) throws CommandException {
        // может приходить /orderservice
        if (isThereNotFinished(appUser, update)) return;
        if (isMainMessage(appUser, update)) return;
        throw new CommandException(Thread.currentThread().getStackTrace());

    }

    @Override
    public boolean isExecuted(AppUser appUser) {
        return true;
    }


    private boolean isMainMessage(AppUser appUser, Update update) {
        Order order = Order.builder().owner(appUser).build();
        try {
            Order existingOrder = cc.findActiveOrder(appUser);
            orderDAO.delete(existingOrder);
        } catch (NoSuchElementException ignored) {
            // Если нет активного заказа, игнорируем исключение
        } finally {
            orderDAO.save(order);
        }

        var out = TextMessage.START.getMessage(appUser.getLanguage());
        List<InlineKeyboardButton> row = new ArrayList<>();
        cc.addButtonToNextStepAndCorrectionButton(row, appUser, userState);
//        cm.sendAnswerEdit(appUser, update, out, new ArrayList<>(List.of(row)));
        cm.sendPhoto(appUser, update, out,
                appUser.getLanguage().equals("ru") ? initialImageRu : initialImageEng, new ArrayList<>(List.of(row)));
        return true;
    }

    private boolean isThereNotFinished(AppUser appUser, Update update) {
        // есть незаконченная заявка

        if (update.hasCallbackQuery() && (!update.getCallbackQuery().getData().equals(userState.getPath())
                || update.getCallbackQuery().getData().equals(veryBegin))) return false;
        try {
            var order = cc.findActiveOrder(appUser);
            List<InlineKeyboardButton> row = new ArrayList<>();
            cm.addButtonToRow(row,
                    FinalizeCommand.userState.getMessage(appUser.getLanguage()),
                    FinalizeCommand.userState.getPath());
            cm.addButtonToRow(row,
                    TextMessage.START_VERY_BEGIN_BUTTON.getMessage(appUser.getLanguage()),
                    veryBegin);
            var out = TextMessage.START_HAS_NOT_FINISHED.getMessage(appUser.getLanguage());
//                cm.sendAnswerEdit(appUser, update, out, new ArrayList<>(List.of(row)));
            cm.sendPhoto(appUser, update, out, unfinishedImage, new ArrayList<>(List.of(row)));
            return true;
        } catch (Exception e) {
            log.warn(e);
            return false;
        }
    }
}
