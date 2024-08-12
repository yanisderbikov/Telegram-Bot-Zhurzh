package com.zhurzh.nodeorderservice.commands;

import com.zhurzh.commonjpa.dao.OrderDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.commonutils.exception.CommandException;
import com.zhurzh.commonutils.model.Command;
import com.zhurzh.nodeorderservice.controller.HasUserState;
import com.zhurzh.nodeorderservice.controller.UserState;
import com.zhurzh.nodeorderservice.ehcache.MyCacheManager;
import com.zhurzh.nodeorderservice.enums.TextMessage;
import com.zhurzh.nodeorderservice.service.CommonCommands;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

@Log4j
@Component
//@AllArgsConstructor
public class ReferencesCommand implements Command, HasUserState {
    @Autowired
    private CommandsManager cm;
    @Autowired
    private CommonCommands cc;
    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private MyCacheManager userCache;

    public static final UserState userState = UserState.REFERENCES;
    private static final String TELEGRAM_FILE_API_URL = "https://api.telegram.org/file/bot";
    @Value("${bot.token}")
    private String botToken; // Замените на токен вашего бота

    @Override
    public UserState getUserState() {
        return userState;
    }
    @Override
    public void execute(AppUser appUser, Update update) throws CommandException {
        if (startCommand(appUser, update)) return;
        if (catcherFilesCommand(appUser, update)) return;
        if (endCommand(appUser, update)) return;
        throw new CommandException(Thread.currentThread().getStackTrace());
    }

    @Override
    public boolean isExecuted(AppUser appUser) {
        var order = cc.findActiveOrder(appUser);
        var ref = order.getArtReference();
        return ref != null && !ref.isEmpty();
    }

    private boolean startCommand(AppUser appUser, Update update) {
        if (update.hasCallbackQuery()) {
            if (!update.getCallbackQuery().getData().equals(userState.getPath())) return false;
            var out = TextMessage.REFERENCE_START.getMessage(appUser.getLanguage());
            cm.sendAnswerEdit(appUser, update, out);
            return true;
        }
        return false;
    }

    private boolean catcherFilesCommand(AppUser appUser, Update update) {
        if (update.hasMessage()){
            String mediaGroupId = update.getMessage().getMediaGroupId();
            String fileId = null;

            if (update.getMessage().hasPhoto()){
                var photo = update.getMessage().getPhoto().stream()
                        .max(Comparator.comparing(PhotoSize::getFileSize))
                        .orElse(null);
                fileId = photo.getFileId();
            } else if (update.getMessage().hasVideo()) {
                var video = update.getMessage().getVideo();
                fileId = video.getFileId();

            } else if (update.getMessage().hasDocument()) {
                var doc = update.getMessage().getDocument();
                fileId = doc.getFileId();
            } else if (update.getMessage().hasText()){
            }

            if (fileId != null) {
                String fileUrl = generateFileDownloadUrl(fileId);
                addUserFile(appUser, fileUrl);
            }else {
                addUserFile(appUser, update.getMessage().getText());
            }

            log.debug(String.format("OUT FOR user '%s' : %s",
                    appUser.getTelegramUserName(), userCache.getReferenceCache(appUser)));

            if (mediaGroupId != null && userCache.checkAndAdd(mediaGroupId)){
                return true;
            }
            var out = TextMessage.REFERENCE_DONE_MES.getMessage(appUser.getLanguage());
            List<InlineKeyboardButton> row = new ArrayList<>();
            cm.addButtonToRow(row,
                    TextMessage.REFERENCE_DONE_BUTTON.getMessage(appUser.getLanguage()),
                    TextMessage.REFERENCE_DONE_BUTTON.name());
            cm.sendAnswerEdit(appUser, update, out, new ArrayList<>(List.of(row)));

            return true;
        }
        return false;
    }

    private boolean endCommand(AppUser appUser, Update update) {
        try {
            if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals(TextMessage.REFERENCE_DONE_BUTTON.name())) {
                var order = cc.findActiveOrder(appUser);
                order.setArtReference(userCache.getReferenceCache(appUser));
                orderDAO.save(order);

                List<InlineKeyboardButton> row = new ArrayList<>();
                List<List<InlineKeyboardButton>> lists = new ArrayList<>();
                cc.addButtonToNextStepAndCorrectionButton(row, appUser, userState);
                lists.add(row);
                userCache.clearReferenceCache(appUser);
                cc.getNextCommandAndExecute(appUser, update);
                return true;
            }
        }catch (Exception e){
            log.error(e);
        }
        return false;
    }

    private String generateFileDownloadUrl(String fileId) {
        var filePath=  getFilePath(fileId);
        return TELEGRAM_FILE_API_URL + botToken + "/" + filePath;
    }


    // звпрос телеграму для получения ссылки

    private String getFilePath(String fileId) {
        String filePath = null;
        String url = "https://api.telegram.org/bot" + botToken + "/getFile?file_id=" + fileId;

        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // Успешный ответ
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Разбор ответа JSON и извлечение filePath
                JSONObject jsonResponse = new JSONObject(response.toString());
                if (jsonResponse.has("result") && jsonResponse.getJSONObject("result").has("file_path")) {
                    filePath = jsonResponse.getJSONObject("result").getString("file_path");
                }
            } else {
                System.out.println("GET запрос не работает");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }

    private void addUserFile(AppUser user, String fileUrl) {

        var text = userCache.getReferenceCache(user);
        userCache.setReferenceCache(user, text + "\n" + fileUrl);

    }


}
