package com.zhurzh.nodepricelist.commands;

import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.commonutils.exception.CommandException;
import com.zhurzh.commonutils.model.Command;
import com.zhurzh.nodepricelist.enums.TextMessage;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@Log4j
public class PriceListCommand implements Command {

    private CommandsManager cm;
    @NonNull
    private final String FA = "https://www.furaffinity.net/user/zhurzh";

    @NonNull
    private final String DA = "https://www.deviantart.com/zhurzh-art";

    @NonNull
    private final String VK = "https://vk.com/art_by_zhurzh";

    @NonNull
    private final String PA = "https://www.patreon.com/ZHURZHDragonartist";
    @NonNull
    private final String BO = "https://boosty.to/zhurzh";
    @NonNull
    private final String TW = "https://twitter.com/zhurzh_art";



    @Override
    public void execute(Update update) throws CommandException {
        var appUser = cm.findOrSaveAppUser(update);
        if (startCommand(update, appUser)) return;
        throw new CommandException(PriceListCommand.class.getName());
    }

    @Override
    public boolean isExecuted(AppUser appUser) {
        return true;
    }

    private boolean startCommand(Update update, AppUser appUser){
        if (update.hasCallbackQuery()){
            var out = TextMessage.PRICE_LIST_MESSAGE.getMessage(appUser.getLanguage());
            List<InlineKeyboardButton> row = new ArrayList<>();
            List<List<InlineKeyboardButton>> list = new ArrayList<>();
            cm.addButtonToRowAsURL(row, "Fur Affinity", FA);
            cm.addButtonToRowAsURL(row, "Deviant Art", DA);
            list.add(row);
            row = new ArrayList<>();

            if (appUser.getLanguage().equals("eng")){
                cm.addButtonToRowAsURL(row, "Patron", PA);
                cm.addButtonToRowAsURL(row, "Twitter", TW);
            }else {
                cm.addButtonToRowAsURL(row, "Boosty", BO);
                cm.addButtonToRowAsURL(row, "Vkontakte", VK);
            }
            list.add(row);
            cm.addButtonToMainMenu(list, appUser);
            cm.sendAnswerEdit(appUser, update, out, list);
            return true;
        }
        return false;
    }
}