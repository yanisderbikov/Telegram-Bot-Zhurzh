package com.zhurzh.nodeorderservice.commands;

import com.zhurzh.commonjpa.dao.OrderDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonjpa.enums.BackgroundOfIllustration;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.commonutils.model.Command;
import com.zhurzh.nodeorderservice.controller.HasUserState;
import com.zhurzh.nodeorderservice.controller.UserState;
import com.zhurzh.nodeorderservice.enums.TextMessage;
import com.zhurzh.commonutils.exception.CommandException;
import com.zhurzh.nodeorderservice.service.CommonCommands;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class BackgroundOfIllustrationCommand implements Command, HasUserState {
    @Autowired
    private CommandsManager cm;
    @Autowired
    private OrderDAO orderDAO;
    //    private UserStateController us;
    @Autowired
    private CommonCommands cc;

    public static final UserState userState = UserState.BACKGROUND;
    @Value("${image.path.background.eng}")
    private String pathImageBackgroundEng;
    @Value("${image.path.background.ru}")
    private String pathImageBackgroundRu;

    @Override
    public UserState getUserState() {
        return userState;
    }

    @Override
    public void execute(AppUser appUser, Update update) throws CommandException {
        if (startCommand(appUser, update)) return;
        if (endCommand(appUser, update)) return;
        throw new CommandException(Thread.currentThread().getStackTrace());
    }

    @Override
    public boolean isExecuted(AppUser appUser) {
        var order = cc.findActiveOrder(appUser);
        return order.getBackgroundOfIllustration() != null;
    }

    private boolean startCommand(AppUser appUser, Update update){
        if (update.hasCallbackQuery()){
            if (!update.getCallbackQuery().getData().equals(userState.getPath())) return false;
            var out = TextMessage.BACKGROUND_START.getMessage(appUser.getLanguage());
            List<List<InlineKeyboardButton>> list = new ArrayList<>();
            cc.addAllButtons(appUser, BackgroundOfIllustration.class, list);
            cm.sendPhoto(appUser, update, out,
                    appUser.getLanguage().equals("ru") ? pathImageBackgroundRu : pathImageBackgroundEng, list);
            return true;
        }
        return false;
    }
    private boolean endCommand(AppUser appUser, Update update) throws CommandException {
        if (update.hasCallbackQuery()){
            var background = BackgroundOfIllustration.values()[
                    Integer.parseInt(update.getCallbackQuery().getData())];
            var out = TextMessage.BACKGROUND_END.getMessage(appUser.getLanguage());
            var order = cc.findActiveOrder(appUser);
            order.setBackgroundOfIllustration(background);
            orderDAO.save(order);
            cc.getNextCommandAndExecute(appUser, update);
            return true;
        }
        return false;
    }
}