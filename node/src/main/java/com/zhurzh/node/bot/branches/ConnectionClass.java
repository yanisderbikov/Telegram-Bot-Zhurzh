package com.zhurzh.node.bot.branches;

import com.zhurzh.model.Branches;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.net.URI;
import java.net.URISyntaxException;


import org.springframework.web.client.RestTemplate;
@Log4j
@Getter
public class ConnectionClass implements Branches {
    private String callbackPath;
    private String url;
    private String port;

    public ConnectionClass(String callbackPath, String url, String port){
        this.port = port;
        this.url = url;
    }
    @Override
    public ResponseEntity<String> isActive(Update update) {
        String path = "/";
        return sendRequest(update, path);
    }


    @Override
    public ResponseEntity<String> manageText(Update update) {
        log.debug("manage Text now");
        String path = "/text";
        return sendRequest(update, path);
    }

    @Override
    public ResponseEntity<String> manageCallBack(Update update) {
        String path = "/callback";
        return sendRequest(update, path);
    }

    private ResponseEntity<String> sendRequest(Update update, String path) {
        try {
            // Создание URL с параметром update
            String urlWithParams = buildUri(path) + "?update=" + update;
            log.debug("uri : " + urlWithParams);

            // Создание RestTemplate
            RestTemplate restTemplate = new RestTemplate();

            // Отправка HTTP GET-запроса на контроллер микросервиса
            ResponseEntity<String> response = restTemplate.getForEntity(urlWithParams, String.class);

            // Обработка ответа, если необходимо
            if (response.getStatusCode().is2xxSuccessful()) {
                // Успешный ответ
                log.debug("Update sent successfully");
            } else {
                // Обработка ошибки
                log.debug("Error sending update");
            }
            return response;
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private URI buildUri(String path) throws URISyntaxException {
        return new URIBuilder()
                .setScheme("http")
                .setPort(Integer.parseInt(port))
                .setHost(url)
                .setPath(path)
                .build();
    }
}
