package ru.lunefox.alphatest.model.rates;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class ExchangeRateHistoryAnalyzer {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateHistoryAnalyzer.class);
    private final ExchangeRateClientBuilder clientBuilder;

    public ExchangeRateHistoryAnalyzer(ExchangeRateClientBuilder clientBuilder) {
        this.clientBuilder = clientBuilder;
    }

    public boolean isRateTodayHigherThanYesterday(String currency) {
        ExchangeRateClient latestClient = clientBuilder.build("latest.json");

        ExchangeRate latestRate = latestClient.find();
        ExchangeRate yesterdayRate = getYesterdayRate(latestRate);

        Double todayCurrencyRate = latestRate.getRates().get(currency);
        Double yesterdayCurrencyRate = yesterdayRate.getRates().get(currency);

        logger.debug("todayCurrencyRate = " + todayCurrencyRate + " " + currency);
        logger.debug("yesterdayCurrencyRate = " + yesterdayCurrencyRate + " " + currency);

        return todayCurrencyRate > yesterdayCurrencyRate;
    }

    private ExchangeRate getYesterdayRate(ExchangeRate latestExchangeRate) {
        long timestamp = latestExchangeRate.getTimestamp();
        String yesterdayDate = getPreviousDayFrom(timestamp);
        ExchangeRateClient yesterdayClient = clientBuilder.build("historical/" + yesterdayDate + ".json");
        return yesterdayClient.find();
    }

    @NotNull
    private String getPreviousDayFrom(long utcSeconds) {
        LocalDateTime now = LocalDateTime.ofEpochSecond(utcSeconds, 0, ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime yesterday = now.minusDays(1);
        return formatter.format(yesterday);
    }
}
