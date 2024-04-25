package com.zhurzh.commonnodeservice.service.impl;

import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonutils.model.Body;
import com.zhurzh.commonutils.model.Branches;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Log4j
@Getter
public class ConnectionToService implements Branches {
    private String callbackPath;
    private String url;

    public ConnectionToService(String callbackPath, String url){
        this.callbackPath = callbackPath;
        this.url = url;
    }
    public ResponseEntity<String> isActive(Body body) {
        var update = body.getUpdate();
        var appUser = body.getAppUser();
        String path = "/";
        var actualUrl = buildUri(path);
        var response =  sendRequest(appUser, update, actualUrl);
        log.debug(String.format("Status : %s, Body : %s, Path : %s", response.getStatusCodeValue(), response.getBody(), actualUrl));
        return response;
    }


    public ResponseEntity<String> execute(Body body) {
        var update = body.getUpdate();
        var appUser = body.getAppUser();
        String path = "/execute";
        var actualUrl = buildUri(path);
        var response =  sendRequest(appUser, update, actualUrl);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.debug(String.format("Status : %s, Body : %s, Url : %s", response.getStatusCodeValue(), response.getBody(), actualUrl));
        }
        return response;
    }



    private ResponseEntity<String> sendRequest(AppUser appUser, Update update, String url) {
        try {
            // Создание URL//            log.debug("URI: " + url);

            // Создание HTTP-заголовков
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Создание HTTP-сущности с объектом Update и заголовками
            HttpEntity<Body> requestEntity = new HttpEntity<>(new Body(appUser, update), headers);

            // Создание RestTemplate
            RestTemplate restTemplate = new RestTemplate();

            // Отправка HTTP POST-запроса на контроллер микросервиса
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            // Обработка ответа, если необходимо
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.debug(String.format("Error sending update with file url: %s\nresponce : %s; %s" ,
                        url, response.getStatusCodeValue(), response.getBody()));
            }
            return response;
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String buildUri(String path) {
        return url + path;
    }
}
