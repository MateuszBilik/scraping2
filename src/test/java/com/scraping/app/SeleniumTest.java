package com.scraping.app;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
class SeleniumTest {

    private static final int SCROLL_DOWN = 20;

    @Test
    void theSameResult_whenUseSelenium() throws IOException, InterruptedException {
        //when
        String url = "https://glodny.pl/bydgoszcz/restauracja-dowozem/85-111";
        List<RestaurantDto> expectedList = getRestaurants(url);

        //given
        List<RestaurantDto> result = new ScrapingService(new SaveData()).getRestaurants(url);

        //then
        Assertions.assertEquals(expectedList.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            Assertions.assertEquals(expectedList.get(i).getName(), result.get(i).getName());
//            Assertions.assertEquals(expectedList.get(i).getAddress(), result.get(i).getAddress());
        }
    }

    private List<RestaurantDto> getRestaurants(String html) throws IOException, InterruptedException {
        confChromeDriver();
        WebDriver driver = getMainPage(html);
        Document doc = Jsoup.parse(driver.getPageSource());
        Elements body = doc.select("div.restaurantContainer");
        return getListOfRestaurants(driver, body);
    }

    private List<RestaurantDto> getListOfRestaurants(WebDriver driver, Elements body) throws InterruptedException {
        List<RestaurantDto> restaurants = new ArrayList<>();
        WebDriverWait wait = new WebDriverWait(driver,5);
        for (Element e : body.select("div.restaurant-repeater")) {
            RestaurantDto rest = createRestaurantDto(driver, e, wait);
            restaurants.add(rest);
        }
        return restaurants;
    }

    private WebDriver getMainPage(String html) throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.get(html);
        for (int i = 0; i < SCROLL_DOWN; i++) {
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.END);
        }
        Thread.sleep(3000);
        return driver;
    }

    private RestaurantDto createRestaurantDto(WebDriver driver, Element e, WebDriverWait wait) throws InterruptedException {
        RestaurantDto rest = new RestaurantDto();
        rest.setName(e.select("img").attr("alt"));

//        driver.get("https://glodny.pl" + e.select("div.left").select("a").attr("href"));
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("details")));
//        Document document = Jsoup.parse(driver.getPageSource());
//        Elements data = document.select("div.details");
//            rest.setAddress(data.select("span.correct").get(2).text());
//            rest.setWorkingHours(data.select("span.correct").get(0).text().replace("Na wynos " ,""));
//            rest.setTimeToDelivery(data.select("div.time").select("span.ng-binding").text());
            return rest;
    }

    private void confChromeDriver() {
        System.setProperty("webdriver.chrome.driver", ".\\chromedriver.exe");
    }
}
