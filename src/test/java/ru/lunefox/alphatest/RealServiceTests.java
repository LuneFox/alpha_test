package ru.lunefox.alphatest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.lunefox.alphatest.model.gifs.Gif;
import ru.lunefox.alphatest.model.gifs.GifService;
import ru.lunefox.alphatest.model.rates.ExchangeRate;
import ru.lunefox.alphatest.model.rates.ExchangeRateService;

import java.util.Map;

@SpringBootTest
public class RealServiceTests {

    private ExchangeRateService exchangeRateService;
    private GifService gifService;

    @Autowired
    public void setExchangeRateClientBuilder(ExchangeRateService exchangeRateClientBuilder,
                                             GifService gifService) {
        this.exchangeRateService = exchangeRateClientBuilder;
        this.gifService = gifService;
    }

    @Test
    public void exchangeRateLoads() {
        ExchangeRate exchangeRate = exchangeRateService.getExchangeRate("latest.json");

        Assertions.assertEquals(exchangeRate.getBase(), "USD");
        Assertions.assertNotEquals(exchangeRate.getTimestamp(), 0);
        Assertions.assertNotNull(exchangeRate.getRates());
        Assertions.assertTrue(exchangeRate.getRates().containsKey("RUB"));
    }

    @Test
    public void gifLoads() {
        Gif gif = gifService.getGif(Gif.Tag.RICH);

        Map<String, Object> data = gif.getData();
        String embedUrl = (String) data.get("embed_url");

        Assertions.assertTrue(embedUrl.contains("https://giphy.com/embed"));
    }
}
