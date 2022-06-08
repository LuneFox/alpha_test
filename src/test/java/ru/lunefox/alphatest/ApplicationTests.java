package ru.lunefox.alphatest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.lunefox.alphatest.model.rates.ExchangeRateClientBuilder;
import ru.lunefox.alphatest.model.rates.ExchangeRateHistoryAnalyzer;

import java.time.LocalDateTime;
import java.lang.reflect.*;
import java.time.ZoneOffset;

@SpringBootTest
class ApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void testDateTransformation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ExchangeRateHistoryAnalyzer analyzer = new ExchangeRateHistoryAnalyzer(new ExchangeRateClientBuilder());

        Method getDateAsString =
                ExchangeRateHistoryAnalyzer.class.getDeclaredMethod(
                        "getDateAsString", LocalDateTime.class);
        getDateAsString.setAccessible(true);

        Method getMinusDayFromTimestamp =
                ExchangeRateHistoryAnalyzer.class.getDeclaredMethod(
                        "getMinusDayFromTimestamp", long.class);
        getMinusDayFromTimestamp.setAccessible(true);

        long timeInUtcSeconds = 1654632000;

        LocalDateTime time = LocalDateTime.ofEpochSecond(timeInUtcSeconds, 0, ZoneOffset.UTC);
        String date = (String) getDateAsString.invoke(analyzer, time);
        String dateMinusDay = (String) getMinusDayFromTimestamp.invoke(analyzer, timeInUtcSeconds);

        Assertions.assertEquals(date, "2022-06-07");
        Assertions.assertEquals(dateMinusDay, "2022-06-06");
    }

}
