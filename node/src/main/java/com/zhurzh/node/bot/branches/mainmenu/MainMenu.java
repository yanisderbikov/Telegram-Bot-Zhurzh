package com.zhurzh.node.bot.branches.mainmenu;

import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.commonutils.model.Branches;
import com.zhurzh.node.bot.branches.ConnectionClass;
import com.zhurzh.node.bot.branches.order.CheckOrderManager;
import com.zhurzh.node.bot.branches.order.OrderManager;
import com.zhurzh.node.bot.branches.pricelist.PriceListManager;
import com.zhurzh.node.bot.branches.start.StartManager;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
@Log4j
public class MainMenu implements Branches {
    private final CommandsManager commandsManager;

    @Autowired
    private ApplicationContext applicationContext;
    @NonNull
    private List<ConnectionClass> connectionClasses;


    @Override
    public ResponseEntity<String> isActive(Update update) {
        return ResponseEntity.ok("ok");
    }

    @Override
    public ResponseEntity<String> manageCallBack(Update update) {
        try {
            manager(update);
            return ResponseEntity.ok("ok");
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> manageText(Update update) {
        try {
            manager(update);
            return ResponseEntity.ok("ok");
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostConstruct
    private void init(){
        connectionClasses = new ArrayList<>();
        Map<String, ConnectionClass> beansOfType = applicationContext.getBeansOfType(ConnectionClass.class);
        connectionClasses.addAll(beansOfType.values());
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
        for (var con : connectionClasses){
            if (con instanceof StartManager) continue;
            var response = con.isActive(update);
            var callbackPath = con.getCallbackPath();
            var body = response.getBody();
            if (response.getStatusCode().is2xxSuccessful()) {
                commandsManager.addButtonToList(list, body, callbackPath);
            }
        }
    }
}
