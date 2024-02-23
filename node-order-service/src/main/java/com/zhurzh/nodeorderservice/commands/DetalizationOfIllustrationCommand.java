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
    @Value("${image.path.detalization.ru}")
    private List<String> imagesRu;

    @NonNull
    @Value("${image.path.detalization.eng}")
    private List<String> imagesEng;
    @NonNull
    private List<InputMedia> listOfImagesRu = new ArrayList<>();
    @NonNull
    private List<InputMedia> listOfImagesEng = new ArrayList<>();

    @PostConstruct
    private void init() {
        for (String imageUrl : imagesRu) {
            listOfImagesRu.add(new InputMediaPhoto(imageUrl));
        }
        if (listOfImagesRu.isEmpty()) throw new RuntimeException("image.path.detalization.ru is empty");

        for (String imageUrl : imagesEng) {
            listOfImagesEng.add(new InputMediaPhoto(imageUrl));
        }
        if (listOfImagesEng.isEmpty()) throw new RuntimeException("image.path.detalization.eng is empty");

    }

    @Override
    public UserState getUserState() {
        return userState;
    }

    @Override
    public void execute(AppUser appUser, Update update) throws CommandException {
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
            cm.sendMedia(appUser, appUser.getLanguage().equals("ru") ? listOfImagesRu : listOfImagesEng);
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

//            for (int i = 1; i <= (appUser.getLanguage().equals("ru") ? listOfImagesRu.size() : listOfImagesEng.size()); i++) {
//                cm.deleteMessage(appUser, update.getCallbackQuery().getMessage().getMessageId() - i);
//            }

            List<InlineKeyboardButton> row = new ArrayList<>();
            cc.addButtonToNextStepAndCorrectionButton(row, appUser, userState);
            var out = TextMessage.DETALIZATION_END.getMessage(appUser.getLanguage());
            cm.sendAnswerEdit(appUser, update, out, new ArrayList<>(List.of(row)));
            return true;
        }
        return false;
    }

}
