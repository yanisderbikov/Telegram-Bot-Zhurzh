package com.zhurzh.groupnode.commands;

import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@AllArgsConstructor
@Component
@Log4j
public class WelcomeCommand implements GroupCommand {
    private final CommandsManager cm;
    @Override
    public void execute(Update update) {
        if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().contains("@ZhurzhBot")) {
            var out = "Привет! \nЯ пока что немного боюсь говорить здесь, но ты можешь пообщаться со мной " +
                    "<a href=\"https://t.me/ZhurzhBot\">лично</a>\n";
            cm.groupSendAnswer(update, out);
        }else {
            if (update.hasMessage() && update.getMessage().hasText()) {
                log.debug(update.getMessage().getText());
            }
        }
    }
}
