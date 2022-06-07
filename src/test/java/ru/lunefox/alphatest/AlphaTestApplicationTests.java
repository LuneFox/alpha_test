package ru.lunefox.alphatest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.lunefox.alphatest.model.gifs.Gif;
import ru.lunefox.alphatest.model.gifs.GifClient;
import ru.lunefox.alphatest.model.gifs.GifClientBuilder;
import ru.lunefox.alphatest.model.gifs.Tag;
import ru.lunefox.alphatest.model.rates.ExchangeRate;
import ru.lunefox.alphatest.model.rates.ExchangeRateClient;
import ru.lunefox.alphatest.model.rates.ExchangeRateClientBuilder;

import java.util.Map;

@SpringBootTest
class AlphaTestApplicationTests {
    private ExchangeRateClientBuilder exchangeRateClientBuilder;
    private GifClientBuilder gifClientBuilder;

    @Autowired
    public void setExchangeRateClientBuilder(ExchangeRateClientBuilder exchangeRateClientBuilder,
                                             GifClientBuilder gifClientBuilder) {
        this.exchangeRateClientBuilder = exchangeRateClientBuilder;
        this.gifClientBuilder = gifClientBuilder;
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void testExchangeRate() {
        ExchangeRateClient latestRatesClient = exchangeRateClientBuilder.build("latest.json");
        ExchangeRate rate = latestRatesClient.find();

        Assertions.assertEquals(rate.getBase(), "USD");
        Assertions.assertNotEquals(rate.getTimestamp(), 0);
        Assertions.assertNotNull(rate.getRates());
        Assertions.assertTrue(rate.getRates().containsKey("RUB"));
    }

    @Test
    public void testGif() {
        GifClient gifClient = gifClientBuilder.build(Tag.RICH);
        Gif gif = gifClient.find();

        Assertions.assertNotNull(gif.getData());

        Map<String, Object> data = gif.getData();
        String embedUrl = (String) data.get("embed_url");

        Assertions.assertTrue(embedUrl.contains("https://giphy.com/embed"));
    }

}
