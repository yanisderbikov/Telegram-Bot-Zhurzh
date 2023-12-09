package com.zhurzh.nodeorderservice.commands;

import com.zhurzh.commonjpa.dao.OrderDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonjpa.entity.Order;
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

@Component
@AllArgsConstructor
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

    /**
     *
     * @param order should be filled all main positions
     * @param appUser to know a language
     * @return calculated string on right lan
     * @throws RuntimeException
     */
    public String calculatePrice(Order order, AppUser appUser) throws RuntimeException{
        double price = 1;
        try {
            // TODO: 09/12/23 Исправить по актуалочке
            price += (order.getBackgroundOfIllustration().ordinal() + 1) * 5;
            price += (order.getDetalizationOfIllustration().ordinal() + 1) * 10;
            price += (order.getCountOfPersons().ordinal() + 1) * 0.2 * price;
            price += (order.getFormatOfIllustration().ordinal() + 1) * 4;

            if (appUser.getLanguage().equals("eng")){
                return String.format("\nAround price is %s$", price);
            }else {
                return String.format("\nПримерная цена %s руб", price * 90);
            }
        }catch (Exception e){
            throw new RuntimeException("Fail to calculate : " + e.getCause().getMessage());
        }
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
            var out = TextMessage.PRICE_START.getMessage(appUser.getLanguage())
                    + calculatePrice(cc.findActiveOrder(appUser), appUser)
                    + TextMessage.PRICE_PAYMENTS.getMessage(appUser.getLanguage());
            cm.sendAnswerEdit(appUser, update, out);
            return true;
        }
        return false;
    }
    private boolean endCommand(AppUser appUser, Update update){
        if (update.hasMessage() && update.getMessage().hasText()){
            var input = update.getMessage().getText();
            var order = cc.findActiveOrder(appUser);
            order.setPrice(input);
            orderDAO.save(order);
            var out = TextMessage.PRICE_END.getMessage(appUser.getLanguage());
            List<InlineKeyboardButton> row = new ArrayList<>();
            cc.addButtonToNextStepAndCorrectionButton(row, appUser, userState);
            cm.sendAnswerEdit(appUser, update, out, new ArrayList<>(List.of(row)));
            return true;
        }
        return false;
    }
}
