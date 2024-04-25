package com.zhurzh.nodecheckorderservice.commands;

import com.zhurzh.commonjpa.dao.OrderDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.commonnodeservice.service.impl.ConnectionToService;
import com.zhurzh.commonutils.exception.CommandException;
import com.zhurzh.commonutils.model.Body;
import com.zhurzh.commonutils.model.Command;
import com.zhurzh.nodecheckorderservice.controller.HasUserState;
import com.zhurzh.nodecheckorderservice.controller.OrderCasheController;
import com.zhurzh.nodecheckorderservice.controller.UserCasheController;
import com.zhurzh.nodecheckorderservice.controller.UserState;
import com.zhurzh.nodecheckorderservice.enums.TextMessage;
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

    @Value("${image.path.empty.order}")
    private String imagePathEmptyOrder;

    @Value("${image.path.found.order}")
    private String imagePathFoundOrder;
    @Value("${order.service.url}")
    private String urlToOrderService;
    @Value("${order.service.callbackpath}")
    private String pathToOrderService;


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
        ConnectionToService connectToOrderService = new ConnectionToService(pathToOrderService, urlToOrderService);
        var response = connectToOrderService.isActive(new Body(appUser, update));
        var body = response.getBody();
        if (response.getStatusCode().is2xxSuccessful() && body != null) {
            InlineKeyboardButton button = new InlineKeyboardButton(body);
            button.setCallbackData(pathToOrderService);
            return List.of(button);
        }else {
            log.warn("Fail to connect to Order service : "+ urlToOrderService + " path: " + pathToOrderService);
            return cm.buttonMainMenu(appUser.getLanguage());
        }

    }

}
