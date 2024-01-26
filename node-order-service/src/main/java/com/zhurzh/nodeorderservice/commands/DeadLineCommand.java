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
import com.zhurzh.nodeorderservice.calendar.CalendarHelper;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.text.SimpleDateFormat;
import java.util.*;

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
    @NonNull
    private Map<Long, Date> dateMap = new HashMap<>();

    @Override
    public UserState getUserState() {
        return userState;
    }

    @Override
    public void execute(Update update) throws CommandException {
        // может приходить /orderservice
        var appUser = cm.findOrSaveAppUser(update);
        if (startCommand(appUser, update)) return;
        if (doesntMatter(appUser, update)) return;
        if (isChangeMonth(appUser, update)) return;
        if (chosenDay(appUser, update)) return;
        if (handlerYesNo(appUser, update)) return;
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

    private boolean doesntMatter(AppUser appUser, Update update) {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals(TextMessage.DOESNT_MATTER.name())){
            Calendar calendar = Calendar.getInstance(new Locale("ru"));
            calendar.add(Calendar.YEAR, 1);
            dateMap.put(appUser.getId(), calendar.getTime());
            update.getCallbackQuery().setData(TextMessage.BUTTON_YES.name());
            return handlerYesNo(appUser, update);
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

    private boolean chosenDay(AppUser appUser, Update update) {
        if (update.hasCallbackQuery()){
            if (update.getCallbackQuery().getData().equals("--")){
                calendarHelper.chooseDays_cmd(appUser, update, map.get(appUser.getId()));
                return true;
            }else {
                try {
                    var day = Integer.parseInt(update.getCallbackQuery().getData());
                    var calendar = Calendar.getInstance(new Locale("ru"));
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    calendar.add(Calendar.MONTH, map.get(appUser.getId()));
                    var date = calendar.getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    dateMap.put(appUser.getId(), date);
                    String formattedDate = dateFormat.format(date);
                    log.debug(formattedDate);
                    var out = TextMessage.DEADLINE_CONFIRM_1.getMessage(appUser.getLanguage()) + formattedDate
                            + TextMessage.DEADLINE_CONFIRM_2.getMessage(appUser.getLanguage());
                    List<InlineKeyboardButton> row = new ArrayList<>();
                    cm.addButtonToRow(row,
                            TextMessage.BUTTON_YES.getMessage(appUser.getLanguage()),
                            TextMessage.BUTTON_YES.name());
                    cm.addButtonToRow(row,
                            TextMessage.BUTTON_NO.getMessage(appUser.getLanguage()),
                            TextMessage.BUTTON_NO.name());
                    cm.sendAnswerEdit(appUser, update, out, List.of(row));
                    return true;
                }catch (Exception e){
                    return false;
                }
            }
        }
        return false;
    }
    private boolean handlerYesNo(AppUser appUser, Update update){
        if (update.hasCallbackQuery()){
            var yes = update.getCallbackQuery().getData().equals(TextMessage.BUTTON_YES.name());
            var no = update.getCallbackQuery().getData().equals(TextMessage.BUTTON_NO.name());
            if (!(yes || no)) return false;
            if (yes){
                // next step
                var order = cc.findActiveOrder(appUser);
                order.setDeadLine(dateMap.get(appUser.getId()));
                orderDAO.save(order);
                var out = TextMessage.DEADLINE_END.getMessage(appUser.getLanguage());
                List<InlineKeyboardButton> row = new ArrayList<>();
                cc.addButtonToNextStepAndCorrectionButton(row, appUser, userState);
                cm.sendAnswerEdit(appUser, update, out, List.of(row));
            }else {
                calendarHelper.chooseDays_cmd(appUser, update, map.get(appUser.getId()));
            }
            return true;
        }
        return false;
    }
}
