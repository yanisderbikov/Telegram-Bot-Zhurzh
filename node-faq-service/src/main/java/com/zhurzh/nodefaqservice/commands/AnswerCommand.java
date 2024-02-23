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
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Log4j
public class AnswerCommand implements Command, HasUserState {
    @Autowired
    private CommandsManager cm;
    @Autowired
    private FAQService faqService;

    @Value("${image.path.faq.ru}")
    private String imagePathRu;

    @Value("${image.path.faq.eng}")
    private String imagePathEng;
    public static UserState userState = UserState.FAQ;
//    @NonNull
//    private final String NEXT_QUESTION = "/next_question";
//    @NonNull
//    private final Map<AppUser, FAQ> map = new HashMap<>();

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
        if (nextQuestion(appUser, update)) return;
        if (endCommand(appUser, update)) return;
        throw new CommandException(this.getClass().getName());
    }

    private boolean startCommand(AppUser appUser, Update update){
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals(userState.getPath())){
            var out = TextMessage.FAQ_START_MESSAGE.getMessage(appUser.getLanguage());
            var faqList = faqService.getFAQsSortedByPopularity(11, appUser.getLanguage());
            List<List<InlineKeyboardButton>> lists = new ArrayList<>();
            for (var faq : faqList){
                cm.addButtonToList(lists, faq.getQuestion(), faq.getId());
            }
            var row = cm.buttonMainMenu(appUser.getLanguage());
            cm.addButtonToRow(row,
                    AddNewQuestionCommand.userState.getMessage(appUser.getLanguage()),
                    AddNewQuestionCommand.userState.getPath());
            lists.add(row);
            cm.sendPhoto(appUser, update, out,
                    appUser.getLanguage().equals("ru") ? imagePathRu : imagePathEng ,
                    lists);
            return true;
        }
        return false;
    }

    private boolean endCommand(AppUser appUser, Update update){
        if (update.hasCallbackQuery()){
            FAQ faq;
            try {
                var faqOPT = faqService.findById(Long.parseLong(update.getCallbackQuery().getData()), appUser);
                if (faqOPT.isEmpty()) return false;
                faq = faqOPT.get();
            }catch (Exception e){
                log.error(e);
                return false;
            }
            var out = build(appUser, faq);
            List<InlineKeyboardButton> row = cm.buttonMainMenu(appUser.getLanguage());
            cm.addButtonToRow(row,
                    TextMessage.NEXT_BUTTON.getMessage(appUser.getLanguage()),
                    TextMessage.NEXT_BUTTON.name());
            List<List<InlineKeyboardButton>> lists = new ArrayList<>();
            lists.add(row);
            cm.addButtonToList(lists,
                    TextMessage.BUTTON_BACK_TO_LIST_QUESTIONS.getMessage(appUser.getLanguage()),
                    AnswerCommand.userState.getPath());
            cm.sendAnswerEdit(appUser, update, out, lists);
            return true;
        }
        return false;
    }

    private boolean nextQuestion(AppUser appUser, Update update){
        if (update.hasCallbackQuery()){
            if (!update.getCallbackQuery().getData().equals(TextMessage.NEXT_BUTTON.name())) return false;
//            var lastFaq = map.get(appUser);
            var opt = faqService.getNextPopularFAQ(appUser);
            var callBackQ = update.getCallbackQuery();
            if (opt.isEmpty()) {
                callBackQ.setData(userState.getPath());
                update.setCallbackQuery(callBackQ);
                return startCommand(appUser, update);
            }
            callBackQ.setData(String.valueOf(opt.get().getId()));
            update.setCallbackQuery(callBackQ);
            return endCommand(appUser, update);
        }
        return false;
    }

    private String build(AppUser appUser, FAQ faq){
        var QUESTION = TextMessage.QUESTION_TITLE.getMessage(appUser.getLanguage());
        var ANSWER = TextMessage.ANSWER_TITLE.getMessage(appUser.getLanguage());

        var out = QUESTION + faq.getQuestion() + "\n\n" +
                ANSWER + faq.getAnswer();
        return out;
    }
}
