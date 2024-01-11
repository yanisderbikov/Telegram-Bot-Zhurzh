package com.zhurzh.commonjpa.service;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CurrencyConverter {

    public static double getUsdToRubRate() {
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

    public static void main(String[] args) {
        double rate = getUsdToRubRate();
        System.out.println("1 USD = " + rate + " RUB");
    }
}
