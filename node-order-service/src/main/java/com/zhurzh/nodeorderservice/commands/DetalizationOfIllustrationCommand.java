package com.zhurzh.nodeorderservice.commands;

import com.zhurzh.commonjpa.dao.OrderDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonjpa.enums.DetalizationOfIllustration;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.commonutils.exception.CommandException;
import com.zhurzh.commonutils.model.Command;
import com.zhurzh.nodeorderservice.controller.HasUserState;
import com.zhurzh.nodeorderservice.controller.UserState;
import com.zhurzh.nodeorderservice.enums.TextMessage;
import com.zhurzh.nodeorderservice.service.CommonCommands;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class DetalizationOfIllustrationCommand implements Command, HasUserState {
    private OrderDAO orderDAO;
    private CommandsManager cm;
    private CommonCommands cc;
    @NonNull
    public static final UserState userState = UserState.DETALIZATION;
    @NonNull
    @Value("${image.gallery.detalization}")
    private List<String> images;
    @NonNull
    private List<InputMedia> listOfImages = new ArrayList<>();

    @PostConstruct
    private void init() {
        for (String imageUrl : images) {
            listOfImages.add(new InputMediaPhoto(imageUrl));
        }
        if (listOfImages.isEmpty()) throw new RuntimeException("image.gallery.detalization is empty");
    }

    @Override
    public UserState getUserState() {
        return userState;
    }

    @Override
    public void execute(Update update) throws CommandException {
        var appUser = cm.findOrSaveAppUser(update);
        if (startCommand(appUser, update)) return;
        if (endCommand(appUser, update)) return;;
        throw new CommandException(Thread.currentThread().getStackTrace());

    }
    @Override
    public boolean isExecuted(AppUser appUser) {
        var order = cc.findActiveOrder(appUser);
        return order.getDetalizationOfIllustration() != null;
    }

    private boolean startCommand(AppUser appUser, Update update){
        if (update.hasCallbackQuery()){
            if (!update.getCallbackQuery().getData().equals(userState.getPath())) return false;
            var out = TextMessage.DETALIZATION_START.getMessage(appUser.getLanguage());
            List<List<InlineKeyboardButton>> lists = new ArrayList<>();
            cc.addAllButtons(appUser, DetalizationOfIllustration.class, lists);
            cm.deleteMessage(appUser, update.getCallbackQuery().getMessage().getMessageId());
            cm.sendMedia(appUser, listOfImages);
            cm.sendAnswerEdit(appUser, null, out, lists);
            return true;
        }
        return false;
    }
    private boolean endCommand(AppUser appUser, Update update){
        if (update.hasCallbackQuery()){
            var detalization = DetalizationOfIllustration.values()
                    [Integer.parseInt(update.getCallbackQuery().getData())];

            var order = cc.findActiveOrder(appUser);
            order.setDetalizationOfIllustration(detalization);
            orderDAO.save(order);

            for (int i = 1; i <= listOfImages.size(); i++) {
                cm.deleteMessage(appUser, update.getCallbackQuery().getMessage().getMessageId() - i);
            }

            List<InlineKeyboardButton> row = new ArrayList<>();
            cc.addButtonToNextStepAndCorrectionButton(row, appUser, userState);
            var out = TextMessage.DETALIZATION_END.getMessage(appUser.getLanguage());
            cm.sendAnswerEdit(appUser, update, out, new ArrayList<>(List.of(row)));
            return true;
        }
        return false;
    }

}
