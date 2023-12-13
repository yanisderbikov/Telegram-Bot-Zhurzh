package com.zhurzh.nodeorderservice.controller;

import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonutils.model.Command;
import com.zhurzh.nodeorderservice.commands.*;
//import com.zhurzh.nodeorderservice.commands.DefaultCommand;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
@Log4j
public class UserStateController {
    @NonNull
    private Map<AppUser, UserState> userStateMap;
    @NonNull
    private static Map<UserState, Command> commandMap;
    @Autowired
    private ApplicationContext applicationContext;


    @PostConstruct
    private void init(){
        userStateMap = new HashMap<>();
        commandMap = new HashMap<>();
        commandMap.put(applicationContext.getBean(BackgroundOfIllustrationCommand.class).getUserState(), applicationContext.getBean(BackgroundOfIllustrationCommand.class));
        commandMap.put(applicationContext.getBean(CommentToArtCommand.class).getUserState(), applicationContext.getBean(CommentToArtCommand.class));
        commandMap.put(applicationContext.getBean(CorrectOrderCommand.class).getUserState(), applicationContext.getBean(CorrectOrderCommand.class));
        commandMap.put(applicationContext.getBean(DetalizationOfIllustrationCommand.class).getUserState(), applicationContext.getBean(DetalizationOfIllustrationCommand.class));
        commandMap.put(applicationContext.getBean(FinalizeCommand.class).getUserState(), applicationContext.getBean(FinalizeCommand.class));
        commandMap.put(applicationContext.getBean(FormatIllustrationCommand.class).getUserState(), applicationContext.getBean(FormatIllustrationCommand.class));
        commandMap.put(applicationContext.getBean(NameCreationCommand.class).getUserState(), applicationContext.getBean(NameCreationCommand.class));
        commandMap.put(applicationContext.getBean(PriceCommand.class).getUserState(), applicationContext.getBean(PriceCommand.class));
        commandMap.put(applicationContext.getBean(ReferencesCommand.class).getUserState(), applicationContext.getBean(ReferencesCommand.class));
        commandMap.put(applicationContext.getBean(StartCommand.class).getUserState(), applicationContext.getBean(StartCommand.class));
        commandMap.put(applicationContext.getBean(CountPersonsCommand.class).getUserState(), applicationContext.getBean(CountPersonsCommand.class));
//        if (UserState.values().length != commandMap.size()) throw new RuntimeException("Not All User State Was Used");
        if (!commandMap.isEmpty()){
            log.debug("All was initialized");
        }else {
            log.error("No initialization of beans Command");
        }
    }

//    @Cacheable(value = "userStateCache", key = "#appUser", unless = "#result == null")
    private UserState getUserState(AppUser appUser) {
        userStateMap.putIfAbsent(appUser, applicationContext.getBean(StartCommand.class).getUserState());
        return userStateMap.get(appUser);
    }

//    @CacheEvict(value = "userStateCache", key = "#appUser")
    public void setUserState(AppUser appUser, UserState state) {
        // Логика установки состояния пользователя
        userStateMap.put(appUser, state);
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
                    userStateMap.put(appUser, v);
                    break;
                }
            }
        }

        var userState = getUserState(appUser);
        return commandMap.get(userState);
    }

    public static Map<UserState, Command> getMapCopy(){
        return new HashMap<>(commandMap);
    }
}