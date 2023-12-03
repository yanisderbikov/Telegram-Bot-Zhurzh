package com.zhurzh.bot.branches.order;

import com.zhurzh.model.Branches;
import lombok.extern.log4j.Log4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.zhurzh.commonjpa.entity.AppUser;

/**
 * Устанавливает связь с сервисом node-order-service
 */
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@Log4j
public class OrderManager implements Branches {

    @Value("${order.service.url}")
    private String orderServiceUrl;

    @Value("${order.service.port}")
    private int orderServicePort;

//    private final WebClient webClient;

//    public OrderManager(WebClient.Builder webClientBuilder) {
//        this.webClient = webClientBuilder.baseUrl(orderServiceUrl + ":" + orderServicePort).build();
//    }

    @Override
    public boolean isActive() {
        // Логика для проверки активности OrderManager
        URIBuilder uriBuilder = new URIBuilder()
                .setScheme("http")
                .setHost(orderServiceUrl)
                .setPath("/");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .uri(uriBuilder.build())
                    .GET()
                    .build();
            log.debug(String.format("url : %s", uriBuilder.build()));
        } catch (URISyntaxException e) {
            log.error(e);
            throw new RuntimeException("Неправильно собралась ссылка");
        }

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                log.debug(response.body());
                return true;
            } else {
                throw new RuntimeException("Не совпадают имя пользователя");
            }
        } catch (IOException | InterruptedException e) {
            log.error(e);
            throw new RuntimeException("ошибка сервера");
        }
    }

    @Override
    public boolean manageCallBack(Update update, AppUser appUser) {
        // Логика для обработки коллбэка и отправки запроса к микросервису Order
        String url = "/api/order";
        // отправка запроса с использованием WebClient
        // webClient.post().uri(url).bodyValue(request).retrieve().bodyToMono(Response.class).block();
        return false;
    }

    @Override
    public boolean manageText(Update update, AppUser appUser) {
        // Логика для обработки текстового сообщения и отправки запроса к микросервису Order
        String url = "/api/order";
        // отправка запроса с использованием WebClient
        // webClient.post().uri(url).bodyValue(request).retrieve().bodyToMono(Response.class).block();
        return false;
    }
}

