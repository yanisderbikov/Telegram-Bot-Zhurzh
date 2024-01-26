package com.zhurzh.nodeorderservice.service;

import com.zhurzh.commonjpa.dao.OrderDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonjpa.entity.Order;
import com.zhurzh.commonjpa.enums.Language;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.nodeorderservice.commands.CorrectOrderCommand;
import com.zhurzh.nodeorderservice.controller.HasUserState;
import com.zhurzh.nodeorderservice.controller.UserState;
import com.zhurzh.nodeorderservice.controller.UserStateController;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
@AllArgsConstructor
@Log4j
public class CommonCommands {
    private CommandsManager cm;
    private OrderDAO orderDAO;

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
        for (var userState : UserState.values()) {
            var commandMap = UserStateController.getMapCopy();
            var command = commandMap.get(userState);
            if (!command.isExecuted(appUser)) {
                return userState;
            }
        }
        return UserState.FINALIZE;
    }
}
