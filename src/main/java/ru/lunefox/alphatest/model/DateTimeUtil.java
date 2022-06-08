package ru.lunefox.alphatest.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public static LocalDateTime secondsToLocalDateTime(long utcSeconds) {
        return LocalDateTime.ofEpochSecond(utcSeconds, 0, ZoneOffset.UTC);
    }

    public static String formatLocalDateTimeAsString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return formatter.format(localDateTime);
    }
}
