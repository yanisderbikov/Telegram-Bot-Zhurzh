package com.zhurzh.node.bot.branches;

import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.model.Branches;
import lombok.extern.log4j.Log4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Log4j
public class ConnectionClass implements Branches {
    private String url;
    private String port;

    public ConnectionClass(String url, String port){
        this.port = port;
        this.url = url;
    }
    @Override
    public ResponseEntity<String> isActive() {
        try {
            URI uri = buildUri("/");
            HttpRequest request = buildGetRequest(uri);
            log.debug(String.format("URL: %s", uri));

            HttpResponse<String> response = sendHttpRequest(request);

            if (response.statusCode() == 200) {
//                log.debug(response.body());
                return ResponseEntity.ok(response.body());
            } else {
                throw new RuntimeException(response.statusCode() + "\nBody: " + response.body());
            }
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
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

    private HttpRequest buildGetRequest(URI uri) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
    }

    private HttpResponse<String> sendHttpRequest(HttpRequest request) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public ResponseEntity<String> manageCallBack(Update update, AppUser appUser) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> manageText(Update update, AppUser appUser) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
