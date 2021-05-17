package com.scraping.app;

import org.json.JSONArray;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ScrapingServiceTest {

    ScrapingService service = new ScrapingService(new SaveData());

    @Test
    void buildUri_whenGivenInputUrl() {
        //given
        URI uriLodz = URI.create("https://api.glodny.pl/restaurants/search?zip=90-517&city=lodz&deliveryType=1");
        URI uriWroclaw = URI.create("https://api.glodny.pl/restaurants/search?zip=50-159&city=wroclaw&deliveryType=1");

        //when
        URI responseLodz = service.buildUri("https://glodny.pl/lodz/restauracja-dowozem/90-517");
        URI responseWroclaw = service.buildUri("https://glodny.pl/wroclaw/restauracja-dowozem/50-159");

        //then
        Assertions.assertEquals(uriLodz, responseLodz);
        Assertions.assertEquals(uriWroclaw, responseWroclaw);
    }

//    @Test
//    void testGetRestaurants() throws IOException, InterruptedException {
//        //given
//        SaveData save = Mockito.mock(SaveData.class);
//        JSONObject jsonObject = new JSONObject("{ dane : [{url : \"asd\"}]}");
//        JSONArray jsonArray = new JSONArray().put(jsonObject);
//
//        ScrapingService scrapingService = new ScrapingService(save);
//        when(jsonObject.getJSONArray(scrapingService.getBody(Mockito.anyString()))).thenReturn(jsonArray);
//        when(scrapingService.getRestaurantsList(jsonArray)).thenReturn(new ArrayList<>());
//
//        //when
//        scrapingService.getRestaurants("url"); //FIXME
//
//        //then
//        Mockito.verify(save, times(1));
//
//    }

    @Test
    void testGetRestaurantsList_GivenTheSameObject() {
        //given
        JSONArray jsonArray = new JSONArray("[\n" +
                "        {\n" +
                "            \"restaurantName\": \"Byczyn\",\n" +
                "            \"location\": \"Poznań\",\n" +
                "            \"metaDescription\": \"Byczyn to wyjątkowa poznańska burgerownia.\",\n" +
                "            \"etaDelivery\": \"35\",\n" +
                "            \"deliveryHours\": \"12:00 - 20:00\",\n" +
                "            \"takeawayHours\": \"12:00 - 20:00\",\n" +
                "            \"contact\": {\n" +
                "                \"address\": \"Górna Wilda 61\",\n" +
                "                \"zip\": \"61-563\",\n" +
                "                \"city\": \"Poznań\",\n" +
                "            },\n" +
                "        }]");
        List<RestaurantDto> expectedList = Arrays.asList(
                new RestaurantDto("Byczyn", "Byczyn to wyjątkowa poznańska burgerownia.",
                        "35", "12:00 - 20:00", "Górna Wilda 61, 61-563 Poznań"));

        //when
        List<RestaurantDto> restaurantsList = service.getRestaurantsList(jsonArray);

        //then
        Assertions.assertEquals(expectedList, restaurantsList);
    }
}
