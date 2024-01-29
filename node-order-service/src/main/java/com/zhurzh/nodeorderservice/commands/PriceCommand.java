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
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@Log4j
public class PriceCommand implements Command, HasUserState {
    private CommandsManager cm;
    private CommonCommands cc;
    private OrderDAO orderDAO;
    @NonNull
    public static final UserState userState = UserState.PRICE;

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
    @Override
    public boolean isExecuted(AppUser appUser) {
        var order = cc.findActiveOrder(appUser);
        return order.getPrice() != null;
    }
    private boolean startCommand(AppUser appUser, Update update){
        try {
            if (update.hasCallbackQuery()) {
                if (!update.getCallbackQuery().getData().equals(userState.getPath())) return false;
                // если что-то незаполнено, необходимо заполнить перед тем как вычислять цену
                if (!cc.findActiveOrder(appUser).isAllFilledExceptPrice()){
                    List<InlineKeyboardButton> row = new ArrayList<>();
                    cm.addButtonToRow(row,
                            CorrectOrderCommand.userState.getMessage(appUser.getLanguage()),
                            CorrectOrderCommand.userState.getPath());
                    cm.sendAnswerEdit(appUser, update,
                            TextMessage.CORRECT_ORDER_FORSED.getMessage(appUser.getLanguage()),
                            new ArrayList<>(List.of(row)));
                    return true;
                }
                var out = cc.findActiveOrder(appUser).calculatePrice()
                        + TextMessage.PRICE_START.getMessage(appUser.getLanguage());

                cm.sendAnswerEdit(appUser, update, out);
                return true;
            }
        }catch (Exception e){
            log.error(e);
        }
        return false;
    }
    private boolean endCommand(AppUser appUser, Update update){
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                var input = update.getMessage().getText();
                if (isNumber(input)) {
                    var order = cc.findActiveOrder(appUser);
                    order.setPrice(input + (appUser.getLanguage().equals("eng") ? " USD" : " Руб"));
                    orderDAO.save(order);
                    var out = TextMessage.PRICE_END.getMessage(appUser.getLanguage());
                    List<InlineKeyboardButton> row = new ArrayList<>();
                    cc.addButtonToNextStepAndCorrectionButton(row, appUser, userState);
                    cm.sendAnswerEdit(appUser, update, out, new ArrayList<>(List.of(row)));
                }else {
                    var out = TextMessage.PRICE_INVALID_NUM.getMessage(appUser.getLanguage());
                    cm.sendAnswerEdit(appUser, update, out);
                }
                return true;
            }
        }catch (Exception e){
            log.error(e);
        }
        return false;
    }
    private boolean isNumber(String str){
        if (str == null || str.isEmpty()) return false;
        try {
            var num = Integer.parseInt(str);
            if (num <= 0) throw new RuntimeException("Less or equals zero");
        }catch (Exception e){
            log.warn("IMPUTED PRICE: " + str);
            return false;
        }
        return true;
    }
}
