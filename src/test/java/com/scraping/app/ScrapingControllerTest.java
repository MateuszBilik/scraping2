package com.scraping.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScrapingController.class)
class ScrapingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScrapingService scrapingService;



    @Test
    void getWebSite() throws Exception {
        //given
        List<RestaurantDto> restaurantsList = getList();
        String url = "https://glodny.pl/lodz/restauracja-dowozem/90-517";
        String json = "[{\"name\":\"Name\",\"description\":\"desc\"," +
                "\"timeToDelivery\":\"delivery\",\"workingHours\":\"hours\",\"address\":\"address\"}]";

        //when
        when(scrapingService.getRestaurants(url))
                .thenReturn(restaurantsList);
        //then
        this.mockMvc.perform(MockMvcRequestBuilders.get("/").param("web", url))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(json));
    }

    private List<RestaurantDto> getList() {
        return Collections.singletonList(
                new RestaurantDto("Name", "desc", "delivery", "hours", "address"));
    }

    @Test
    void getWebSite_whenThrowException() throws Exception {
        //given
        String url = "https://glodny.pl/lodz/restauracja-dowozem/20-111";

        //when
        when(scrapingService.getRestaurants(url))
                .thenThrow(new IOException());
        //then
        this.mockMvc.perform(MockMvcRequestBuilders.get("/").param("web", url))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string("[]"));
    }
}