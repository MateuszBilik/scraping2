package com.scraping.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
@Slf4j
public class ScrapingService {


    private String getBody(String requestURL) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(buildUri(requestURL))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    private URI buildUri(String url) {
        return URI.create(new StringBuilder("https://api.glodny.pl/restaurants/search?zip=")
                .append(url.substring(url.length() - 6))
                .toString());
    }

    public List<RestaurantDto> getRestaurants(String requestURL) throws IOException, InterruptedException {
        JSONArray jsonArray = new JSONObject(getBody(requestURL)).getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); i++) {
            new RestaurantDto(
                    jsonArray.getJSONObject(i).getString("restaurantName"),
                    jsonArray.getJSONObject(i).getString("metaDescription"),
                    jsonArray.getJSONObject(i).getString("deliveryHours"),
                    jsonArray.getJSONObject(i).getString("deliveryHours"),

            )
        }

    }
}