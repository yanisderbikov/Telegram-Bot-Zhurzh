package com.zhurzh.app.nodefaqservice.commands;

import com.zhurzh.app.commonjpa.dao.FAQRepository;
import com.zhurzh.app.commonjpa.entity.AppUser;
import com.zhurzh.app.commonjpa.entity.FAQ;
import com.zhurzh.app.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.app.commonutils.exception.CommandException;
import com.zhurzh.app.commonutils.model.Command;
import com.zhurzh.app.nodefaqservice.controller.CacheController;
import com.zhurzh.app.nodefaqservice.controller.HasUserState;
import com.zhurzh.app.nodefaqservice.controller.UserState;
import com.zhurzh.app.nodefaqservice.enums.TextMessage;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@Log4j
public class AddNewQuestionCommand implements Command, HasUserState {
    private CommandsManager cm;
    private FAQRepository faqRepository;

    private CacheController cacheController;
    public static UserState userState = UserState.ADD_NEW_QUESTION;

    @Override
    public UserState getUserState() {
        return userState;
    }

    @Override
    public boolean isExecuted(AppUser appUser) {
        return true;
    }

    @Override
    public void execute(AppUser appUser, Update update) throws CommandException {
        if (startCommand(appUser, update)) return;
        if (confirm(appUser, update)) return;
        if (confirmByButton(appUser, update)) return;
        throw new CommandException(this.getClass().getName());
    }

    private boolean startCommand(AppUser appUser, Update update) {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals(userState.getPath())){
            var out = TextMessage.ADD_NEW_QUESTION_START.getMessage(appUser.getLanguage());
            cm.sendAnswerEdit(appUser, update, out);
            return true;
        }
        return false;
    }

    private boolean confirm(AppUser appUser, Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            var input = update.getMessage().getText();
            var out = String.format("%s\n\n<b><i>«%s»</i></b>",
                    TextMessage.ADD_NEW_QUESTION_CONFIRM.getMessage(appUser.getLanguage()),
                    input);

            List<List<InlineKeyboardButton>> list = new ArrayList<>();
            List<InlineKeyboardButton> row = new ArrayList<>();
            cm.addButtonToRow(row,
                    TextMessage.BUTTON_YES.getMessage(appUser.getLanguage()),
                    TextMessage.BUTTON_YES.name());
            cm.addButtonToRow(row,
                    TextMessage.BUTTON_NO.getMessage(appUser.getLanguage()),
                    TextMessage.BUTTON_NO.name());
            list.add(row);
            cm.addButtonToList(list,
                    TextMessage.BUTTON_CANCEL.getMessage(appUser.getLanguage()),
                    AnswerCommand.userState.getPath());
            cacheController.setQuestion(appUser, input);
            cm.sendAnswerEdit(appUser, update, out, list);
            return true;
        }
        return false;
    }

    private boolean confirmByButton(AppUser appUser, Update update) {
        if (update.hasCallbackQuery()){
            var isYES = update.getCallbackQuery().getData().equals(TextMessage.BUTTON_YES.name());
            var isNO = update.getCallbackQuery().getData().equals(TextMessage.BUTTON_NO.name());
            if (!(isYES || isNO)) return false;
            if (isYES){
                save(appUser, update);
            }else {
                update.getCallbackQuery().setData(userState.getPath());
                startCommand(appUser, update);
            }
            return true;
        }
        return false;
    }

    private void save(AppUser appUser, Update update){
        var input = cacheController.getQuestion(appUser);
        FAQ faq = FAQ.builder()
                .question(input)
                .language(appUser.getLanguage())
                .fromUserId(appUser.getTelegramUserId())
                .build();
        faqRepository.save(faq);
        var out = TextMessage.ADD_NEW_QUESTION_END.getMessage(appUser.getLanguage());
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        cm.addButtonToList(lists,
                TextMessage.BUTTON_BACK_TO_LIST_QUESTIONS.getMessage(appUser.getLanguage()),
                AnswerCommand.userState.getPath());
        cm.sendAnswerEdit(appUser, update, out, lists);
    }
}
