package ru.lunefox.alphatest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.lunefox.alphatest.model.rates.ExchangeRate;
import ru.lunefox.alphatest.model.rates.ExchangeRateClient;
import ru.lunefox.alphatest.model.rates.ExchangeRateClientBuilder;

@SpringBootTest
class AlphaTestApplicationTests {
    private ExchangeRateClientBuilder builder;

    @Autowired
    public void setBuilder(ExchangeRateClientBuilder builder) {
        this.builder = builder;
    }

    @Test
    void contextLoads() {
    }

    @Test
    void testExchangeRate() {
        ExchangeRateClient latestRatesClient = builder.build("latest.json");
        ExchangeRate rate = latestRatesClient.find();

        Assertions.assertEquals(rate.getBase(), "USD");
        Assertions.assertNotEquals(rate.getTimestamp(), 0);
        Assertions.assertNotNull(rate.getRates());
        Assertions.assertTrue(rate.getRates().containsKey("RUB"));
    }

}
