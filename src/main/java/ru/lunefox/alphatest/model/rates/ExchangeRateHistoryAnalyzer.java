package ru.lunefox.alphatest.model.rates;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class ExchangeRateHistoryAnalyzer {
    private String currency;

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateHistoryAnalyzer.class);
    private final ExchangeRateClientBuilder clientBuilder;

    public ExchangeRateHistoryAnalyzer(ExchangeRateClientBuilder clientBuilder) {
        this.clientBuilder = clientBuilder;
    }

    public boolean isRateTodayHigherThanYesterday(String requestedCurrency) {
        this.currency = requestedCurrency;

        ExchangeRate todayRate = getTodayRate();

        if (!todayRate.containsCurrency(currency)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong currency specified.");
        }

        ExchangeRate yesterdayRate = getYesterdayRate(todayRate);

        return compareRates(todayRate, yesterdayRate);
    }

    private ExchangeRate getTodayRate() {
        ExchangeRateClient todayClient = clientBuilder.build("latest.json");
        return todayClient.find();
    }

    private ExchangeRate getYesterdayRate(ExchangeRate todayRate) {
        long timestamp = todayRate.getTimestamp();
        String dateFromDayBefore = getMinusDayFromTimestamp(timestamp);
        ExchangeRateClient yesterdayClient = clientBuilder.build("historical/" + dateFromDayBefore + ".json");
        return yesterdayClient.find();
    }

    @NotNull
    private String getMinusDayFromTimestamp(long utcSeconds) {
        LocalDateTime now = LocalDateTime.ofEpochSecond(utcSeconds, 0, ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime yesterday = now.minusDays(1);
        return formatter.format(yesterday);
    }

    private boolean compareRates(ExchangeRate todayRate, ExchangeRate yesterdayRate) {
        Double today = todayRate.getRates().get(currency);
        Double yesterday = yesterdayRate.getRates().get(currency);
        logger.debug("today = " + today + " " + currency);
        logger.debug("yesterday = " + yesterday + " " + currency);
        return today > yesterday;
    }
}