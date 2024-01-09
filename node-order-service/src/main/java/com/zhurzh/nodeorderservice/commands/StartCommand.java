package com.zhurzh.nodeorderservice.commands;

import com.zhurzh.commonjpa.dao.OrderDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonjpa.entity.Order;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.commonutils.exception.CommandException;
import com.zhurzh.commonutils.model.Command;
import com.zhurzh.nodeorderservice.controller.HasUserState;
import com.zhurzh.nodeorderservice.controller.UserState;
import com.zhurzh.nodeorderservice.enums.TextMessage;
import com.zhurzh.nodeorderservice.service.CommonCommands;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
//import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Log4j
@Component
//@AllArgsConstructor
public class StartCommand implements Command, HasUserState {
    @Autowired
    private CommandsManager cm;
    @Autowired
    private CommonCommands cc;
    @Autowired
    private OrderDAO orderDAO;
//    @NonNull
    public static final UserState userState = UserState.START;
//    @NonNull
    public static final String veryBegin = "/very_begin";
//    @NonNull
    @Value("${image.path.unfinished.order}")
    private String unfinishedImage;

//    @NonNull
    @Value("${image.path.initial.order}")
    private String initialImage;
    @Override
    public UserState getUserState() {
        return userState;
    }

    @Override
    public void execute(Update update) throws CommandException {
        // может приходить /orderservice
        var appUser = cm.findOrSaveAppUser(update);
        if (isThereNotFinished(appUser, update)) return;
        if (isMainMessage(appUser, update)) return;
        throw new CommandException(Thread.currentThread().getStackTrace());

    }
    @Override
    public boolean isExecuted(AppUser appUser) {
        return true;
    }



    private boolean isMainMessage(AppUser appUser, Update update){
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
        cm.sendPhoto(appUser, update, out, initialImage, new ArrayList<>(List.of(row)));
        return true;
    }

    private boolean isThereNotFinished(AppUser appUser, Update update){
        // есть незаконченная заявка
        if (update.hasCallbackQuery()){
            if (!update.getCallbackQuery().getData().equals(userState.getPath())
             || update.getCallbackQuery().getData().equals(veryBegin)) return false;
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
            }catch (Exception e){
                log.error(e);
                return false;
            }
        }
        return false;
    }

}
