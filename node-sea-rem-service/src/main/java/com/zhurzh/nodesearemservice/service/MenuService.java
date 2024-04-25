package com.zhurzh.nodesearemservice.service;

import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.commonutils.exception.CommandException;
import com.zhurzh.nodesearemservice.dao.SeaRemFaqRepository;
import com.zhurzh.nodesearemservice.entity.SeaRemFAQ;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j
@AllArgsConstructor
public class MenuService {
    private CommandsManager cm;
    private SeaRemFaqRepository seaRemFaqRepository;

    public void execute(AppUser appUser, Update update) throws CommandException {
        if (start(appUser, update)) return;
        saveBD(appUser, update);

    }

    private void saveBD(AppUser appUser, Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            SeaRemFAQ seaRemFAQ = SeaRemFAQ.builder()
                    .fromUserId(appUser.getTelegramUserId())
                    .question(update.getMessage().getText())
                    .creationDate(LocalDate.now())
                    .language(appUser.getLanguage())
                    .build();
            seaRemFaqRepository.save(seaRemFAQ);

            var list = seaRemFaqRepository.findAll();
            if (list.isEmpty()) System.out.println("EMPTY");
            System.out.println(list);
        }
    }

    private boolean start(AppUser appUser, Update update){
        if (!update.hasMessage()) {
            var out = "Lor of Sea Rem";
            List<List<InlineKeyboardButton>> list = new ArrayList<>();
            cm.addButtonToMainMenu(list, appUser);
            cm.sendAnswerEdit(appUser, update, out, list);
            return true;
        }
        return false;
    }

}
