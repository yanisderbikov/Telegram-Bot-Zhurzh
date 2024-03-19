package com.zhurzh.commonjpa.service;
import lombok.extern.log4j.Log4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Log4j
@Component
public class CurrencyConverter {

    private static double actualCurrency = getUsdToRubRate();

    public static double getActualCurrency(){
        log.debug(actualCurrency);
        return actualCurrency;
    }

    private static double getUsdToRubRate() {
        String apiKey = "7f49b555db234a6db3cab96fedafdcb7";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://openexchangerates.org/api/latest.json?app_id=" + apiKey))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            // Извлекаем курс из JSON ответа (простой подход без использования внешних библиотек)
            String rubValue = responseBody.split("\"RUB\":")[1].split(",")[0];
            return Double.parseDouble(rubValue);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return 90;
    }

//      @Scheduled(cron = "0 * * * * *"): Запускать каждую минуту.
//      @Scheduled(cron = "0 0 8 * * *"): Запускать каждый день в 8:00 утра.
//      @Scheduled(cron = "0 0/30 8-10 * * *"): Запускать каждые 30 минут между 8:00 и 10:59 утра.
//      @Scheduled(cron = "0 0 9 * * MON-FRI"): Запускать в 9:00 утра с понедельника по пятницу.
//      @Scheduled(cron = "0 0 9 1 * *"): Запускать в 9:00 утра первого числа каждого месяца.

    @Scheduled(cron = "0 0 9 * * *")
    public void updateCurrency(){
        actualCurrency = getUsdToRubRate();
        log.debug("Updated currency : "  + actualCurrency);
    }
}
