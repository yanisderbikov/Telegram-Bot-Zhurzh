package com.zhurzh.nodeorderservice.commands;


import com.zhurzh.commonjpa.dao.OrderDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.commonutils.exception.CommandException;
import com.zhurzh.commonutils.model.Command;
import com.zhurzh.nodeorderservice.controller.HasUserState;
import com.zhurzh.nodeorderservice.controller.UserState;
import com.zhurzh.nodeorderservice.enums.TextMessage;
import com.zhurzh.nodeorderservice.service.CommonCommands;
import com.zhurzh.nodeorderservice.servicecommand.CalendarHelper;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
@Log4j
public class DeadLineCommand implements Command, HasUserState {
    private CommandsManager cm;
    private CalendarHelper calendarHelper;
    private CommonCommands cc;
    private OrderDAO orderDAO;
    @NonNull
    public static final UserState userState = UserState.DEADLINE;
    @NonNull
    private Map<Long, Integer> map = new HashMap<>();

    @Override
    public UserState getUserState() {
        return userState;
    }

    @Override
    public void execute(Update update) throws CommandException {
        // может приходить /orderservice
        var appUser = cm.findOrSaveAppUser(update);
        if (startCommand(appUser, update)) return;
        if (isChangeMonth(appUser, update)) return;
        throw new CommandException(Thread.currentThread().getStackTrace());

    }
    @Override
    public boolean isExecuted(AppUser appUser) {
        return cc.findActiveOrder(appUser).getDeadLine() != null;
    }

    private boolean startCommand(AppUser appUser, Update update){
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals(userState.getPath())){
            map.putIfAbsent(appUser.getId(), 0);
            calendarHelper.chooseDays_cmd(appUser, update, map.get(appUser.getId()));
            return true;
        }
        return false;
    }

    private boolean isChangeMonth(AppUser appUser, Update update){
        if (update.hasCallbackQuery()){
            var prev = update.getCallbackQuery().getData().equals(TextMessage.PREV_MO.name());
            var next = update.getCallbackQuery().getData().equals(TextMessage.NEXT_MO.name());
            if (!(prev || next)) return false;
            if (next) map.put(appUser.getId(), map.get(appUser.getId()) + 1);
            if (prev) map.put(appUser.getId(), map.get(appUser.getId()) - 1);
            calendarHelper.chooseDays_cmd(appUser, update, map.get(appUser.getId()));
            return true;
        }
        return false;
    }


}
