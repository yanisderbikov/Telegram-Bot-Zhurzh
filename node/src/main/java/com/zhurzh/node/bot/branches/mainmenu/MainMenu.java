package com.zhurzh.node.bot.branches.mainmenu;

import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.commonutils.model.Body;
import com.zhurzh.commonutils.model.Branches;
import com.zhurzh.commonnodeservice.service.impl.ConnectionToService;
import com.zhurzh.node.bot.branches.searem.SeaRemBranch;
import com.zhurzh.node.bot.branches.start.StartManager;
import com.zhurzh.node.service.ConnectionAppUser;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@Log4j
public class MainMenu implements Branches {
    @Autowired
    private CommandsManager commandsManager;
    @Autowired
    private ConnectionAppUser connectionAppUser;

    @Autowired
    private ApplicationContext applicationContext;
    private List<ConnectionToService> connectionToServices;

    @Value("${image.menu.url.ru}")
    private String linkRu;
    @Value("${image.menu.url.eng}")
    private String linkEng;


    @Override
    public ResponseEntity<String> isActive(Body body) {
        return ResponseEntity.ok("ok");
    }

    @Override
    public ResponseEntity<String> execute(Body body) {
        try {
            manager(body.getUpdate());
            return ResponseEntity.ok("ok");
        }catch (Exception e){
            var list = Arrays.asList(e.getStackTrace());
            log.error(list);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostConstruct
    private void init(){
        connectionToServices = new ArrayList<>();
        Map<String, ConnectionToService> beansOfType = applicationContext.getBeansOfType(ConnectionToService.class);
        connectionToServices.addAll(beansOfType.values());
    }

    /**
     * Создание основного меню
     */
    private void manager(Update update){
        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        var appUser = connectionAppUser.findOrSaveAppUser(update);
        var lan = appUser.getLanguage();
        if (lan == null) throw new RuntimeException("No language for user : " + connectionAppUser.findOrSaveAppUser(update));
        addButtons(list, update, appUser);
        if (list.isEmpty()){
            commandsManager.sendAnswerEdit(appUser, update, TextMessage.NO_SERVICE_AVAILABLE.getMessage(appUser.getLanguage()));
        }else {
            commandsManager.sendPhoto(appUser, update, null, lan.equals("ru") ? linkRu : linkEng, list);
        }
    }

    private void addButtons(List<List<InlineKeyboardButton>> list, Update update, AppUser appUser){
        for (var con : connectionToServices){
            if (con instanceof StartManager) continue;
            if (con instanceof SeaRemBranch
                    && appUser.getTelegramUserName() != null
                    && !appUser.getTelegramUserName().equals("yanderbikov"))  {
                continue;
            }
            var response = con.isActive(new Body(appUser, update));
            var callbackPath = con.getCallbackPath();
            var body = response.getBody();
            if (response.getStatusCode().is2xxSuccessful()) {
                commandsManager.addButtonToList(list, body, callbackPath);
            }
        }
    }
}
