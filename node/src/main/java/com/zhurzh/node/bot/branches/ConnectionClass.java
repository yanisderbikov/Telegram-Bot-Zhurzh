package com.zhurzh.node.bot.branches;

import com.zhurzh.commonutils.model.Branches;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.http.ResponseEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Log4j
@Getter
public class ConnectionClass implements Branches {
    private String callbackPath;
    private String url;
    private String port;

    public ConnectionClass(String callbackPath, String url, String port){
        this.callbackPath = callbackPath;
        this.port = port;
        this.url = url;
    }
    @Override
    public ResponseEntity<String> isActive(Update update) {
        String path = "/";
        var response =  sendRequest(update, path);
        log.debug(String.format("Status : %s, Body : %s", response.getStatusCodeValue(), response.getBody()));
        return response;
    }


    @Override
    public ResponseEntity<String> execute(Update update) {
        String path = "/execute";
        var response =  sendRequest(update, path);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.debug(String.format("Status : %s, Body : %s", response.getStatusCodeValue(), response.getBody()));
        }
        return response;
    }



    private ResponseEntity<String> sendRequest(Update update, String path) {
        try {
            // Создание URL
            var url = buildUri(path);
//            log.debug("URI: " + url);

            // Создание HTTP-заголовков
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Создание HTTP-сущности с объектом Update и заголовками
            HttpEntity<Update> requestEntity = new HttpEntity<>(update, headers);

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

    private URI buildUri(String path) throws URISyntaxException {
        return new URIBuilder()
                .setScheme("http")
                .setPort(Integer.parseInt(port))
                .setHost(url)
                .setPath(path)
                .build();
    }
}
