package ru.lunefox.alphatest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.lunefox.alphatest.model.DateTimeUtil;

import java.time.LocalDateTime;

@SpringBootTest
class ApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void testDateTransformation() {
        long timeInUtcSeconds = 1654632000;

        LocalDateTime timeStamp = DateTimeUtil.secondsToLocalDateTime(timeInUtcSeconds);
        LocalDateTime timeStampMinusOneDay = timeStamp.minusDays(1);

        String date = DateTimeUtil.formatLocalDateTimeAsString(timeStamp);
        String dateMinusDay = DateTimeUtil.formatLocalDateTimeAsString(timeStampMinusOneDay);

        Assertions.assertEquals(date, "2022-06-07");
        Assertions.assertEquals(dateMinusDay, "2022-06-06");
    }

}
