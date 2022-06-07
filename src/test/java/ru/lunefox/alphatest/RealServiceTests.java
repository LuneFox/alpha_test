package ru.lunefox.alphatest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.lunefox.alphatest.model.gifs.Gif;
import ru.lunefox.alphatest.model.gifs.GifClient;
import ru.lunefox.alphatest.model.gifs.GifClientBuilder;
import ru.lunefox.alphatest.model.rates.ExchangeRate;
import ru.lunefox.alphatest.model.rates.ExchangeRateClient;
import ru.lunefox.alphatest.model.rates.ExchangeRateClientBuilder;

import java.util.Map;

@SpringBootTest
public class RealServiceTests {

    private ExchangeRateClientBuilder exchangeRateClientBuilder;
    private GifClientBuilder gifClientBuilder;

    @Autowired
    public void setExchangeRateClientBuilder(ExchangeRateClientBuilder exchangeRateClientBuilder,
                                             GifClientBuilder gifClientBuilder) {
        this.exchangeRateClientBuilder = exchangeRateClientBuilder;
        this.gifClientBuilder = gifClientBuilder;
    }

    @Test
    public void exchangeRateLoads() {
        ExchangeRateClient exchangeRateClient = exchangeRateClientBuilder.build("latest.json");
        ExchangeRate exchangeRate = exchangeRateClient.find();

        Assertions.assertEquals(exchangeRate.getBase(), "USD");
        Assertions.assertNotEquals(exchangeRate.getTimestamp(), 0);
        Assertions.assertNotNull(exchangeRate.getRates());
        Assertions.assertTrue(exchangeRate.getRates().containsKey("RUB"));
    }

    @Test
    public void gifLoads() {
        GifClient gifClient = gifClientBuilder.build(Gif.Tag.RICH);
        Gif gif = gifClient.find();

        Map<String, Object> data = gif.getData();
        String embedUrl = (String) data.get("embed_url");

        Assertions.assertTrue(embedUrl.contains("https://giphy.com/embed"));
    }
}
