package com.zhurzh.node.bot.branches.mainmenu;

import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.model.Branches;
import com.zhurzh.node.bot.branches.order.CheckOrderManager;
import com.zhurzh.node.bot.branches.order.OrderManager;
import com.zhurzh.node.bot.branches.start.StartManager;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@Log4j
public class MainMenu implements Branches {
    private final CommandsManager commandsManager;
    private StartManager startManager;
    private CheckOrderManager checkOrderManager;
    private OrderManager orderManager;


    @Override
    public ResponseEntity<String> isActive(Update update) {
        return ResponseEntity.ok("ok");
    }

    @Override
    public ResponseEntity<String> manageCallBack(Update update) {
        manager(update);
        return ResponseEntity.ok("ok");
    }

    @Override
    public ResponseEntity<String> manageText(Update update) {
        manager(update);
        return ResponseEntity.ok("ok");
    }

    /**
     * Создание основного меню
     */
    private void manager(Update update){
        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        var appUser = commandsManager.findOrSaveAppUser(update);
        var len = appUser.getLanguage();
        if (len == null) throw new RuntimeException("No language for user : " + commandsManager.findOrSaveAppUser(update));
        addButtons(list, update);
        if (list.isEmpty()){
            commandsManager.sendAnswerEdit(appUser, update, TextMessage.NO_SERVICE_AVAILABLE.getMessage(appUser.getLanguage()));
        }else {
            commandsManager.sendAnswerEdit(appUser, update, TextMessage.MENU.getMessage(len), list);
        }
    }

    private void addButtons(List<List<InlineKeyboardButton>> list, Update update){
        var response = orderManager.isActive(update);
        if (response.getStatusCode().is2xxSuccessful()) commandsManager.addButtonToList(list, response.getBody(), orderManager.getCallbackPath());
        response = checkOrderManager.isActive(update);
        if (response.getStatusCode().is2xxSuccessful()) commandsManager.addButtonToList(list, response.getBody(), checkOrderManager.getCallbackPath());
    }
}
