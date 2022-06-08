package ru.lunefox.alphatest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.lunefox.alphatest.model.gifs.Gif;
import ru.lunefox.alphatest.model.gifs.GifService;
import ru.lunefox.alphatest.model.rates.ExchangeRate;
import ru.lunefox.alphatest.model.rates.ExchangeRateService;

@SpringBootTest
public class RealServiceTests {

    private final ExchangeRateService exchangeRateService;
    private final GifService gifService;

    @Autowired
    public RealServiceTests(ExchangeRateService exchangeRateService, GifService gifService) {
        this.exchangeRateService = exchangeRateService;
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
        String embedUrl = gif.getEmbedUrl();

        Assertions.assertTrue(embedUrl.contains("https://giphy.com/embed"));
    }
}
