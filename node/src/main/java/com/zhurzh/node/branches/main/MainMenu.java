package com.zhurzh.node.branches.main;

import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonjpa.enums.BranchStatus;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.commonutils.model.Body;
import com.zhurzh.commonutils.model.Branch;
import com.zhurzh.node.service.ConnectionAppUser;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Log4j
public class MainMenu extends Branch {

    private final CommandsManager commandsManager;
    private final ConnectionAppUser connectionAppUser;
    private final ApplicationContext applicationContext;
    private List<Branch> branches;

    @Value("${image.menu.url.ru}")
    private String linkRu;
    @Value("${image.menu.url.eng}")
    private String linkEng;

    public MainMenu(CommandsManager commandsManager, ConnectionAppUser connectionAppUser, ApplicationContext applicationContext) {
        super(BranchStatus.MENU);
        this.commandsManager = commandsManager;
        this.connectionAppUser = connectionAppUser;
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void initBranches() {
        Map<String, Branch> branchBeans = applicationContext.getBeansOfType(Branch.class);
        this.branches = new ArrayList<>(branchBeans.values());
        log.info("Loaded branches: %s".formatted(branches));
    }


    @Override
    public String isActiveAndGetButtonName(Body body) {
        return TextMessage.MENU.getMessage(body.getAppUser().getLanguage());
    }

    @Override
    public void execute(Body body) {
        try {
            manager(body.getUpdate());
        }catch (Exception e){
            log.error(e);
        }
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

    private void addButtons(List<List<InlineKeyboardButton>> list, Update update, AppUser appUser) {
        if (branches == null || branches.isEmpty()) throw new RuntimeException("no %s beans realization".formatted(Branch.class));
        for (var branch : branches){
            commandsManager.addButtonToList(
                    list,
                    branch.isActiveAndGetButtonName(new Body(appUser, update)),
                    branch.getPath());

        }
    }
}
