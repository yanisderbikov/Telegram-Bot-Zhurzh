package com.zhurzh.nodeorderservice.calendar;

import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.nodeorderservice.enums.TextMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

@Component
@AllArgsConstructor
public class CalendarHelper {
    private CommandsManager cm;
    private Map<Long, Integer> mapUserIdDay = new HashMap<>();

    public void chooseDays_cmd(AppUser appUser, Update update, Integer plusMo) {
//        var output = "Выбери даты в которые хочешь работать, а затем нажми " + CHOOSE_DAY_IS_DONE + "\nдля отмены - " + CANCEL + "\n\n";

        InlineKeyboardMarkup markupInLine = createCalendar(plusMo, appUser);
        var a = Calendar.getInstance(new Locale("ru"));
        a.add(Calendar.MONTH, plusMo);
        int mo = (a.get(Calendar.MONTH) + 1);
        var text = formatOfDate(appUser.getLanguage(), a);
//                appUser.getLanguage().equals("ru") ? String.format("Выбор на %s месяц", mo) :
//                String.format("Choose for %s month", mo);
        cm.sendAnswerEdit(appUser, update, text, markupInLine);
    }

    private InlineKeyboardMarkup createCalendar(Integer plusMonth, AppUser appUser) {

        Calendar calendar = Calendar.getInstance(new Locale("ru"));
        calendar.add(Calendar.MONTH, plusMonth);

        int today = calendar.get(Calendar.DAY_OF_MONTH); // Получаем текущий день месяца
        calendar.set(Calendar.DAY_OF_MONTH, 1); // Устанавливаем день месяца на 1

//        if (plusMonth > 0){
//            calendar.set(Calendar.DAY_OF_MONTH, 1);
//            today = 1;
//        }

        // Определяем, с какого дня недели начинается месяц
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 2; // Вычитаем 2, так как нумерация начинается с воскресенья

        // Если первый день недели - воскресенье, сдвигаем на 7 дней назад
        if (firstDayOfWeek == -1) {
            firstDayOfWeek = 6;
        }

        List<List<InlineKeyboardButton>> calendarList = new ArrayList<>();
        List<InlineKeyboardButton> currentWeek = new ArrayList<>();
        calendarList.add(createButtonListDays(appUser.getLanguage()));
        // Выводим прочерки до текущей даты
        for (int i = 0; i < firstDayOfWeek && i < today - 1; i++) {
            currentWeek.add(createButton("--", "--"));
        }

        Calendar calendarHelperNow = Calendar.getInstance();
        calendarHelperNow.add(Calendar.MONTH, plusMonth);
        if (plusMonth >= 1){
            calendarHelperNow.set(Calendar.DAY_OF_MONTH, 1);
        }

        // Создаем объект Calendar для первого дня месяца
        Calendar firstDayOfMonth = (Calendar) calendar.clone();
        firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1);

        // Создаем объект Calendar для последнего дня месяца
        Calendar lastDayOfMonth = (Calendar) calendar.clone();
        lastDayOfMonth.set(Calendar.DAY_OF_MONTH, lastDayOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH));

        Calendar calendarNow = Calendar.getInstance(new Locale("ru"));

        // Выводим дни месяца
        boolean isLastWeek = false;
        while (calendar.get(Calendar.MONTH) == calendarHelperNow.get(Calendar.MONTH) || !isLastWeek) {
            // для наставников
//            var button =
//                    dayOfMonth > calendarHelperNow.getActualMaximum(Calendar.DAY_OF_MONTH) || dayOfMonth < calendarHelperNow.get(Calendar.DAY_OF_MONTH)
//                    ? createButton() : createButton(dayOfMonth, dayOfMonth);

            boolean isOutOfRange = calendar.before(firstDayOfMonth) || calendar.after(lastDayOfMonth) || calendar.before(calendarNow);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            var button = isOutOfRange ? createButton() : createButton(dayOfMonth, dayOfMonth);

            currentWeek.add(button);

            calendar.add(Calendar.DAY_OF_MONTH, 1);

            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                calendarList.add(currentWeek);
                currentWeek = new ArrayList<>();
                if (isLastWeek) break;
                if (calendar.get(Calendar.MONTH) != calendarHelperNow.get(Calendar.MONTH)) {
                    isLastWeek = true;
                }
            }
        }

        // Добавляем последнюю неделю
        calendarList.add(currentWeek);

        List<InlineKeyboardButton> row = new ArrayList<>();
        cm.addButtonToRow(row,
                TextMessage.PREV_MO.getMessage(appUser.getLanguage()),
                TextMessage.PREV_MO.name());
        cm.addButtonToRow(row,
                TextMessage.NEXT_MO.getMessage(appUser.getLanguage()),
                TextMessage.NEXT_MO.name());
        calendarList.add(row);
        cm.addButtonToList(calendarList,
                TextMessage.DOESNT_MATTER.getMessage(appUser.getLanguage()),
                TextMessage.DOESNT_MATTER.name());
        return new InlineKeyboardMarkup(calendarList);
    }
    /**
     *
     * @param text вид кнопки
     * @param callback колбек кнопки
     * @return число
     */

    private InlineKeyboardButton createButton(String text, String callback) {
        var button = new InlineKeyboardButton(text);
        button.setCallbackData(callback);
        return button;
    }

    /**
     *
     * @return -- --
     */
    private InlineKeyboardButton createButton() {
        var button = new InlineKeyboardButton("--");
        button.setCallbackData("--");
        return button;
    }

    private InlineKeyboardButton createButton(int day) {
        var button = new InlineKeyboardButton("+");
        button.setCallbackData(String.valueOf(day));
        return button;
    }

    /**
     *
     * @param text вид кнопки
     * @param callback колбек кнопки
     * @return число
     */
    private InlineKeyboardButton createButton(int text, int callback) {
        return createButton(String.valueOf(text), String.valueOf(callback));
    }

    private List<InlineKeyboardButton> createButtonListDays(String len) {
        List<InlineKeyboardButton> list = new ArrayList<>();
        var week = len.equals("ru") ? Arrays.asList("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс") : Arrays.asList("MO", "TU", "WE", "TU", "FR", "SU", "SA");
        for (var a : week) {
            list.add(createButton(a, "--"));
        }
        return list;
    }
    private String formatOfDate(String lan, Calendar calendar){
        String out = "";
        if (lan.equals("ru")){
            out = String.format("Выбор на %s %s",
                    MonthName.values()[calendar.get(Calendar.MONTH)].getMessage(lan), calendar.get(Calendar.YEAR));
        }else {
            out = String.format("Choose for %s of %s",
                    MonthName.values()[calendar.get(Calendar.MONTH)].getMessage(lan), calendar.get(Calendar.YEAR));
        }
        return out;
    }
}
