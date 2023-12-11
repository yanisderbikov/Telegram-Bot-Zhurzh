package com.zhurzh.nodeorderservice.service;

import com.zhurzh.commonjpa.dao.OrderDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonjpa.entity.Order;
import com.zhurzh.commonjpa.enums.Language;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.nodeorderservice.commands.CorrectOrderCommand;
import com.zhurzh.nodeorderservice.controller.UserState;
import com.zhurzh.nodeorderservice.controller.UserStateController;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class CommonCommands {
    private CommandsManager cm;
    private OrderDAO orderDAO;
    public boolean addCorrectButtonToRow(List<InlineKeyboardButton> row, AppUser appUser){
        var order = findActiveOrder(appUser);
        if (
                order.getBackgroundOfIllustration() != null &&
                order.getCountOfPersons() != null &&
                order.getDetalizationOfIllustration() != null &&
                order.getPrice() != null &&
                order.getName() != null &&
                        order.getCommentToArt() != null &&
                        order.getReference() != null
        ) {
            cm.addButtonToRow(row,
                    CorrectOrderCommand.userState.getMessage(appUser.getLanguage()),
                    CorrectOrderCommand.userState.getPath());
            return true;
        }
        return false;
    }
    public Order findActiveOrder(AppUser appUser){
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
        if (!row.isEmpty()){
            lists.add(row);
        }
    }
    public void addButtonToNextStepAndCorrectionButton(List<InlineKeyboardButton> row, AppUser appUser,
                                                       UserState userState) throws RuntimeException{
        var currentState = userState.ordinal();
        if (currentState + 1 > UserState.values().length) {
            throw new RuntimeException("This was a final step in UserState : "
                    + userState.getMessage(appUser.getLanguage()));
        }
        var nextState = UserState.values()[currentState + 1];
        cm.addButtonToRow(row, nextState.getMessage(appUser.getLanguage()),
                nextState.getPath());
        if (nextState != CorrectOrderCommand.userState) {
            addCorrectButtonToRow(row, appUser);
        }
    }

}
