package com.zhurzh.nodefaqservice.commands;

import com.zhurzh.commonjpa.dao.FAQRepository;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonjpa.entity.FAQ;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.commonutils.exception.CommandException;
import com.zhurzh.commonutils.model.Command;
import com.zhurzh.nodefaqservice.controller.HasUserState;
import com.zhurzh.nodefaqservice.controller.UserState;
import com.zhurzh.nodefaqservice.enums.TextMessage;
import com.zhurzh.nodefaqservice.service.FAQService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@Log4j
public class AddNewQuestionCommand implements Command, HasUserState {
    private CommandsManager cm;
    private FAQRepository faqRepository;

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
    public void execute(Update update) throws CommandException {
        var appUser = cm.findOrSaveAppUser(update);
        if (startCommand(appUser, update)) return;
        if (endCommand(appUser, update)) return;
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

    private boolean endCommand(AppUser appUser, Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            var input = update.getMessage().getText();
            FAQ faq = FAQ.builder()
                    .question(input)
                    .language(appUser.getLanguage())
                    .fromUserId(appUser.getTelegramUserId())
                    .build();
            faqRepository.save(faq);
            var out = TextMessage.ADD_NEW_QUESTION_END.getMessage(appUser.getLanguage());
            List<List<InlineKeyboardButton>> lists = new ArrayList<>();
            cm.addButtonToMainMenu(lists, appUser);
            cm.sendAnswerEdit(appUser, update, out, lists);
            return true;
        }
        return false;
    }
}
