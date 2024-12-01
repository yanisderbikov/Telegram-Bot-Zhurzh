package com.zhurzh.app.nodeorderservice.commands;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zhurzh.app.commonjpa.dao.OrderDAO;
import com.zhurzh.app.commonjpa.entity.AppUser;
import com.zhurzh.app.commonjpa.enums.AdditionsOptionsEnum;
import com.zhurzh.app.commonjpa.enums.DetalizationOfIllustration;
import com.zhurzh.app.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.app.commonutils.exception.CommandException;
import com.zhurzh.app.commonutils.model.Command;
import com.zhurzh.app.nodeorderservice.controller.HasUserState;
import com.zhurzh.app.nodeorderservice.controller.UserState;
import com.zhurzh.app.nodeorderservice.enums.TextMessage;
import com.zhurzh.app.nodeorderservice.service.CommonCommands;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class AdditionalOptions implements Command, HasUserState {
    private OrderDAO orderDAO;
    private CommandsManager cm;
    private CommonCommands cc;

    @NonNull
    public static final UserState userState = UserState.ADDITIONAL_OPTIONS;

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
    private Cache<AppUser, Set<Integer>> chosenButtonCache = CacheBuilder.newBuilder()
                    .maximumSize(100)
                    .expireAfterWrite(1, TimeUnit.HOURS)
                    .build();

    public AdditionalOptions(OrderDAO orderDAO, CommandsManager cm, CommonCommands cc) {
        this.orderDAO = orderDAO;
        this.cm = cm;
        this.cc = cc;
    }

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
        if (endCommand(appUser, update)) return;
        if (startCommand(appUser, update)) return;
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
            var out = TextMessage.ADDITIONAL_OPTIONS_MESSAGE_1.getMessage(appUser.getLanguage());
            List<List<InlineKeyboardButton>> lists = setChosenButtons(appUser, update);
            cm.addButtonToList(lists,
                    TextMessage.ADDITIONAL_BUTTON_DONE.getMessage(appUser.getLanguage()),
                    TextMessage.ADDITIONAL_BUTTON_DONE.toString());

            cm.sendAnswerEdit(appUser, null, out, lists);
            return true;
        }
        return false;
    }
    private boolean endCommand(AppUser appUser, Update update) throws CommandException {
        if (!update.hasCallbackQuery() && !update.getCallbackQuery().getData().equals(TextMessage.ADDITIONAL_BUTTON_DONE)) return false;

        var order = cc.findActiveOrder(appUser);

        var list =
        for (var val : Objects.requireNonNull(chosenButtonCache.getIfPresent(appUser))) {

        }
        order.setAdditionsOptions();

        return false;
    }

    private List<List<InlineKeyboardButton>> setChosenButtons(AppUser appUser, Update update) {
        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        int chosen = Integer.parseInt(update.getCallbackQuery().getData());
        var set = chosenButtonCache.getIfPresent(appUser);
        Set<Integer> chosenButtons = set != null ? set : new HashSet<>();

        if (chosenButtons.contains(chosen)) {
            chosenButtons.remove(chosen);
        } else {
            chosenButtons.add(chosen);
        }
        chosenButtonCache.put(appUser, chosenButtons);

        for (AdditionsOptionsEnum option : AdditionsOptionsEnum.values()) {
            String text = option.getMessage(appUser.getLanguage());
            if (chosenButtons.contains(option.ordinal())) {
                text = "+" + text;
            }
            cm.addButtonToList(list, text, option.ordinal());
        }
        return list;
    }

}
