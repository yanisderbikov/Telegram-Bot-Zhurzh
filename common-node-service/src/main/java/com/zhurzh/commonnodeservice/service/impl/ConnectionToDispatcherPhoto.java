package com.zhurzh.commonnodeservice.service.impl;

import lombok.Value;
import lombok.extern.log4j.Log4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.net.URI;
import java.net.URISyntaxException;

@Component
@Log4j
public class ConnectionToDispatcherPhoto {

    private String port = "8999";
    private String url = "localhost";

    public ResponseEntity<String> sendRequest(SendPhoto sendPhoto) {
        try {
            // Создание URL
            var url = buildUri("/photo");

            // Создание HTTP-заголовков
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Создание HTTP-сущности с объектом Update и заголовками
            HttpEntity<SendPhoto> requestEntity = new HttpEntity<>(sendPhoto, headers);

            // Создание RestTemplate
            RestTemplate restTemplate = new RestTemplate();

            // Отправка HTTP POST-запроса на контроллер микросервиса
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            // Обработка ответа, если необходимо
            if (!response.getStatusCode().is2xxSuccessful()) {
                // Обработка ошибки
                log.debug("Error sending sendPhoto \n responce body: " + response.getBody());
            }
            return response;
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<String> sendMedia(SendMediaGroup sendPhoto) {
        try {
            // Создание URL
            var url = buildUri("/media");
//            log.debug("URI: " + url);

            // Создание HTTP-заголовков
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Создание HTTP-сущности с объектом Update и заголовками
            HttpEntity<SendMediaGroup> requestEntity = new HttpEntity<>(sendPhoto, headers);

            // Создание RestTemplate
            RestTemplate restTemplate = new RestTemplate();

            // Отправка HTTP POST-запроса на контроллер микросервиса

            return restTemplate.postForEntity(url, requestEntity, String.class);
        } catch (Exception e) {
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
