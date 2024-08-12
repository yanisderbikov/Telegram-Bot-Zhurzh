package com.zhurzh.app.nodecheckorderservice.commands;

import com.zhurzh.app.commonjpa.dao.OrderDAO;
import com.zhurzh.app.commonjpa.entity.AppUser;
import com.zhurzh.app.commonjpa.enums.BranchStatus;
import com.zhurzh.app.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.app.commonutils.exception.CommandException;
import com.zhurzh.app.commonutils.model.Body;
import com.zhurzh.app.commonutils.model.Command;
import com.zhurzh.app.nodecheckorderservice.enums.TextMessage;
import com.zhurzh.app.nodecheckorderservice.controller.HasUserState;
import com.zhurzh.app.nodecheckorderservice.controller.UserState;
import com.zhurzh.app.nodeorderservice.controller.OrderServiceController;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@Log4j
public class ChooseOrderCommand implements Command, HasUserState {

    @Autowired
    private CommandsManager cm;
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private ViewOrderCommand viewOrderCommand;

    @Autowired
    private OrderServiceController orderServiceController;

    @Value("${image.path.empty.order}")
    private String imagePathEmptyOrder;

    @Value("${image.path.found.order}")
    private String imagePathFoundOrder;


    @NonNull
    public static final UserState userState = UserState.CHOOSE_ORDER;
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
        return false;
    }

    private boolean startCommand(AppUser appUser, Update update){
        if (update.hasCallbackQuery()){
            if (!update.getCallbackQuery().getData().equals(userState.getPath())) return false;
            var orders = orderDAO.findByOwner(appUser).stream()
                    .filter(e -> e.getIsFinished() != null && e.getIsFinished())
                    .toList();
            List<List<InlineKeyboardButton>> lists = new ArrayList<>();
            if (orders.isEmpty()){
                var out = TextMessage.FAIL_FIND_ORDER.getMessage(appUser.getLanguage()); // here
                lists.add(getButtonToOrder(appUser, update));
                cm.addButtonToMainMenu(lists, appUser);
                cm.sendPhoto(appUser, update, out, imagePathEmptyOrder, lists);
                return true;
            }
            var out = TextMessage.CHOOSE_ORDER_START.getMessage(appUser.getLanguage());
            for (var order : orders){
                cm.addButtonToList(lists, order.getName(), order.getId());
            }
            cm.addButtonToMainMenu(lists, appUser);
            cm.sendPhoto(appUser, update, out, imagePathFoundOrder, lists);
            return true;
        }
        return false;
    }

    private boolean endCommand(AppUser appUser, Update update) {
        if (update.hasCallbackQuery()){
            var id = Long.parseLong(update.getCallbackQuery().getData());
            var order = orderDAO.findById(id)
                    .orElseThrow();
            viewOrderCommand.showOrder(appUser, update, order);
            return true;
        }
        return false;
    }

    private List<InlineKeyboardButton> getButtonToOrder(AppUser appUser, Update update){

        var response = orderServiceController.isActiveAndGetButtonName(new Body(appUser, update));
        if (response != null && !response.isEmpty()) {
            InlineKeyboardButton button = new InlineKeyboardButton(response);
            button.setCallbackData(BranchStatus.ORDER.getPath());
            return List.of(button);
        }else {
            return cm.buttonMainMenu(appUser.getLanguage());
        }
    }
}
