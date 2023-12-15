package com.zhurzh.nodefaqservice.controller;

import com.zhurzh.commonjpa.dao.OrderDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonutils.model.Command;
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
public class UserCasheController {
    private OrderDAO orderDAO;
    @NonNull
    private Map<AppUser, UserState> userStateMap;
    @NonNull
    private Map<UserState, Command> commandMap;
    @Autowired
    private ApplicationContext applicationContext;


    @PostConstruct
    private void init(){
        userStateMap = new HashMap<>();
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
        }
    }

//    @Cacheable(value = "userStateCache", key = "#appUser", unless = "#result == null")
    public UserState getUserState(AppUser appUser) {
        // Логика получения состояния пользователя
        // может быть несколько заказов, но текущая только одна
        if (!userStateMap.containsKey(appUser)){
            userStateMap.put(appUser, UserState.FAQ);
        }
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



}