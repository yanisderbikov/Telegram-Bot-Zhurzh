package com.zhurzh.nodeorderservice.service;

import com.zhurzh.commonjpa.dao.OrderDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonjpa.entity.Order;
import com.zhurzh.commonjpa.enums.Language;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.commonutils.exception.CommandException;
import com.zhurzh.nodeorderservice.commands.CorrectOrderCommand;
import com.zhurzh.nodeorderservice.commands.FinalizeCommand;
import com.zhurzh.nodeorderservice.controller.UserState;
import com.zhurzh.nodeorderservice.controller.UserStateController;
import com.zhurzh.nodeorderservice.ehcache.MyCacheManager;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

@Component
@AllArgsConstructor
@Log4j
public class CommonCommands {
    private CommandsManager cm;
    private OrderDAO orderDAO;
    private MyCacheManager cacheManager;
    public boolean addCorrectButtonToRow(List<InlineKeyboardButton> row, AppUser appUser) {
        try {
            var order = findActiveOrder(appUser);
            if (order.isAllFilled()) {
                cm.addButtonToRow(row,
                        CorrectOrderCommand.userState.getMessage(appUser.getLanguage()),
                        CorrectOrderCommand.userState.getPath());
                return true;
            }
        } catch (Exception e) {
            log.debug("No active order for " + appUser);
        }
        return false;
    }

    public Order findActiveOrder(AppUser appUser) throws NoSuchElementException {
        var list = orderDAO.findByOwner(appUser);
        return list.stream()
                .filter(o -> !o.getIsFinished())
                .findFirst()
                .orElseThrow();
    }

    /**
     * Добавляет все возможные кнопки в от enumClass в lists
     */
    public <T extends Enum<T> & Language> void addAllButtons(AppUser appUser, Class<T> enumClass,
                                                             List<List<InlineKeyboardButton>> lists) {
        List<InlineKeyboardButton> row = new ArrayList<>();
        T[] enumConstants = enumClass.getEnumConstants();

        for (int i = 0; i < enumConstants.length; i++) {
            T cur = enumConstants[i];
            if (i != 0 && i % 2 == 0) {
                lists.add(row);
                row = new ArrayList<>();
            }
            cm.addButtonToRow(row, cur.getMessage(appUser.getLanguage()), cur.ordinal());
        }
        if (!row.isEmpty()) {
            lists.add(row);
        }
    }

    public void addButtonToNextStepAndCorrectionButton(List<InlineKeyboardButton> row, AppUser appUser,
                                                       UserState userState) throws RuntimeException {
        var currentState = userState.ordinal();
        UserState nextState;
        if (currentState + 1 > UserState.values().length) {
            log.warn("This was a final step in UserState : "
                    + userState.getMessage(appUser.getLanguage()));
            nextState = UserState.FINALIZE;
        } else {
            nextState = fff(appUser);
        }
        cm.addButtonToRow(row,
                nextState.getMessage(appUser.getLanguage()),
                nextState.getPath());
    }

    private UserState fff(AppUser appUser) {
        var commandMap = UserStateController.getMapCopy();
        for (var userState : UserState.values()) {
            var command = commandMap.get(userState);
            if (!command.isExecuted(appUser)) {
                return userState;
            }
        }
        return UserState.FINALIZE;
    }

    /**
     *
     * @param appUser
     * @param update изменяется и устанавливается след
     * @return
     */

    public void getNextCommandAndExecute(AppUser appUser, Update update) throws CommandException {
        var commandMap = UserStateController.getMapCopy();
        for (var userState : UserState.values()) {
            var command = commandMap.get(userState);
            if (!command.isExecuted(appUser)) {
                cacheManager.setStateCache(appUser, userState);
                setCallBack(update, userState.getPath());
                command.execute(appUser, update);
                return;
            }
        }
        UserState finalUserState = FinalizeCommand.userState;
        cacheManager.setStateCache(appUser, finalUserState);
        setCallBack(update, finalUserState.getPath());
        commandMap.get(finalUserState).execute(appUser, update);
    }

    private void setCallBack(Update update, String callbackMessage){
        if (update.hasCallbackQuery()){
            update.getCallbackQuery().setData(callbackMessage);
        }else {
            var callback = new CallbackQuery();
            callback.setData(callbackMessage);
            update.setCallbackQuery(callback);
        }
    }

}
