package com.zhurzh.nodeorderservice.controller;

import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonutils.model.Command;
//import com.zhurzh.nodeorderservice.commands.DefaultCommand;
import com.zhurzh.nodeorderservice.commands.StartCommand;
import com.zhurzh.nodeorderservice.ehcache.MyCacheManager;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@Log4j
public class UserStateController {
    private static Map<UserState, Command> commandMap;
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MyCacheManager cacheManager;



    @PostConstruct
    private void init(){
        commandMap = new HashMap<>();

        Map<String, Command> commandBeansMap = applicationContext.getBeansOfType(Command.class);
        Map<String, HasUserState> userStateBeansMap = applicationContext.getBeansOfType(HasUserState.class);

        for (Map.Entry<String, Command> entry : commandBeansMap.entrySet()) {
            String beanName = entry.getKey();
            Command command = entry.getValue();

            // Проверяем, реализует ли бин также AnotherInterface
            if (userStateBeansMap.containsKey(beanName)) {
                var state = userStateBeansMap.get(beanName);
                commandMap.put(state.getUserState(), command);
            }

        }

        if (!commandMap.isEmpty()){
            log.debug("All was initialized");
        }else {
            log.error("No initialization of beans Command");
            throw new RuntimeException("No initialization of beans Command");
        }
    }


    /**
     * фкнкция определяет к какому состоянию относится текущее сообщение и запоминает состояние пользователя
     * @return
     */
    public Command getCommand(AppUser appUser, Update update){

        if (update.hasCallbackQuery()){
            var data = update.getCallbackQuery().getData();

            for (var v : UserState.values()){
                if (v.getPath().equals(data)){
                    cacheManager.setStateCache(appUser, v);
                    break;
                }
            }
            if (data.equals(StartCommand.veryBegin)){
                cacheManager.setDefaultState(appUser);
            }
        }

        var userState = cacheManager.getStateCache(appUser);
        return commandMap.get(userState);
    }

    public static Map<UserState, Command> getMapCopy(){
        return new HashMap<>(commandMap);
    }
}