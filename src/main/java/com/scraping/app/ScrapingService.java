package com.scraping.app;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ScrapingService {

   private final SaveData saveData;
   private static final int ZIP_CODE_LENGTH = 6;

    public ScrapingService(SaveData saveData)    {
        this.saveData = saveData;
    }

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
                .append(url.substring(url.length() - ZIP_CODE_LENGTH))
                .append("&city=")
                .append(url.substring(18, url.length() - 27)) //TODO
                .append("&deliveryType=1")
                .toString());
    }

    public List<RestaurantDto> getRestaurants(String requestURL) throws IOException, InterruptedException {
        JSONArray jsonArray = new JSONObject(getBody(requestURL)).getJSONArray("data");
        List<RestaurantDto> list = getRestaurantsList(jsonArray);
        log.info("Scraped records: " + list.size());
        saveData.save(list.toString());
        return list;
    }

    private List<RestaurantDto> getRestaurantsList(JSONArray jsonArray) {
        List<RestaurantDto> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject address = jsonArray.getJSONObject(i).getJSONObject("contact");
            list.add(new RestaurantDto(
                    jsonArray.getJSONObject(i).getString("restaurantName"),
                    jsonArray.getJSONObject(i).getString("metaDescription"),
                    jsonArray.getJSONObject(i).getString("etaDelivery"),
                    jsonArray.getJSONObject(i).getString("deliveryHours"),
                    address.getString("address") + ", " +
                            address.getString("zip") + " " +
                            address.getString("city")
            ));
        }
        return list;
    }
}
