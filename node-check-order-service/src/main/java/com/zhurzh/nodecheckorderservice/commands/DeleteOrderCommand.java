package com.zhurzh.nodecheckorderservice.commands;

import com.zhurzh.commonjpa.dao.OrderDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonjpa.entity.Order;
import com.zhurzh.commonjpa.enums.StatusZhurzh;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.commonutils.exception.CommandException;
import com.zhurzh.commonutils.model.Command;
import com.zhurzh.nodecheckorderservice.controller.HasUserState;
import com.zhurzh.nodecheckorderservice.controller.OrderCasheController;
import com.zhurzh.nodecheckorderservice.controller.UserCasheController;
import com.zhurzh.nodecheckorderservice.controller.UserState;
import com.zhurzh.nodecheckorderservice.enums.TextMessage;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Log4j
@AllArgsConstructor
public class DeleteOrderCommand implements Command, HasUserState {
    private CommandsManager cm;
    private OrderDAO orderDAO;
    private OrderCasheController orderCasheController;
    @NonNull
    public static final UserState userState = UserState.DELETE_ORDER;
    @Override
    public UserState getUserState() {
        return userState;
    }

    @Override
    public void execute(AppUser appUser, Update update) throws CommandException {
        if (endCommand(appUser, update)) return;
        if (checkChosenOrderCommand(appUser, update)) return;
        throw new CommandException(Thread.currentThread().getStackTrace());
    }

    @Override
    public boolean isExecuted(AppUser appUser) {
        return false;
    }

//    private boolean startCommand(AppUser appUser, Update update) {
//        if (update.hasCallbackQuery()){
//            if (!update.getCallbackQuery().getData().equals(userState.getPath())) return false;
//            var out = TextMessage.DELETE_ORDER_START.getMessage(appUser.getLanguage());
//            // выводится список заказов, которые не подтверждены
//            var orders = orderDAO.findByOwner(appUser).stream()
//                    .filter(e -> e.getStatusZhurzh() == StatusZhurzh.UNSEEN)
//                    .filter(Order::getIsFinished)
//                    .toList();
//            List<List<InlineKeyboardButton>> lists = new ArrayList<>();
//            for (var order : orders){
//                cm.addButtonToList(lists, order.getName(), order.getId());
//            }
//            cm.addButtonToList(lists,
//                    TextMessage.BUTTON_BACK.getMessage(appUser.getLanguage()),
//                    ChooseOrderCommand.userState.getPath());
//
//            cm.sendAnswerEdit(appUser, update, out, lists);
//            return true;
//        }
//        return false;
//    }

    private boolean checkChosenOrderCommand(AppUser appUser, Update update) {
        if (update.hasCallbackQuery()){
            try {
//                Long idOrder = Long.parseLong(update.getCallbackQuery().getData());
                orderCasheController.getCurrentOrder(appUser);
            }catch (Exception e){
                return false;
            }
            var out = TextMessage.DELETE_ORDER_CHECK.getMessage(appUser.getLanguage());
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
        if (update.hasCallbackQuery()){
            var isYes = update.getCallbackQuery().getData().equals(TextMessage.BUTTON_YES.name());
            var isNo = update.getCallbackQuery().getData().equals(TextMessage.BUTTON_NO.name());
            if (!(isYes || isNo)) return false;


            List<List<InlineKeyboardButton>> lists = new ArrayList<>();
            var out = "";

            if (isYes) {
                Order order;
                try {
                    order = orderCasheController.getCurrentOrder(appUser);
                } catch (Exception e) {
                    log.error(e);
                    return false;
                }
                orderDAO.delete(order);
                out = TextMessage.DELETE_ORDER_DELETED.getMessage(appUser.getLanguage());
            }else {
                out = TextMessage.DELETE_ORDER_NOT_DELETED.getMessage(appUser.getLanguage());
            }
            cm.addButtonToMainMenu(lists, appUser);
            cm.sendAnswerEdit(appUser, update, out, lists);
            return true;
        }
        return false;
    }

}
