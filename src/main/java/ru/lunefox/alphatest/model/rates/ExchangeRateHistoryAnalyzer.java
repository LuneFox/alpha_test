package ru.lunefox.alphatest.model.rates;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.lunefox.alphatest.model.DateTimeUtil;

import java.time.LocalDateTime;

public class ExchangeRateHistoryAnalyzer {
    private String currency;

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateHistoryAnalyzer.class);
    private final ExchangeRateService service;

    public ExchangeRateHistoryAnalyzer(ExchangeRateService service) {
        this.service = service;
    }

    public boolean isRateTodayHigherThanYesterday(String requestedCurrency) {
        this.currency = requestedCurrency.toUpperCase();

        ExchangeRate todayExchangeRate = getTodayExchangeRate();

        if (!todayExchangeRate.containsCurrency(currency)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong currency specified.");
        }

        ExchangeRate yesterdayExchangeRate = getYesterdayExchangeRate(todayExchangeRate);

        return compareExchangeRates(todayExchangeRate, yesterdayExchangeRate);
    }

    private ExchangeRate getTodayExchangeRate() {
        return service.getExchangeRate("latest.json");
    }

    private ExchangeRate getYesterdayExchangeRate(ExchangeRate todayRate) {
        String historicalDate = getYesterdayDate(todayRate);
        return service.getExchangeRate("historical/" + historicalDate + ".json");
    }

    @NotNull
    private String getYesterdayDate(ExchangeRate todayRate) {
        long todayTimeStamp = todayRate.getTimestamp();
        LocalDateTime today = DateTimeUtil.secondsToLocalDateTime(todayTimeStamp);
        LocalDateTime yesterday = today.minusDays(1);
        return DateTimeUtil.formatLocalDateTimeAsString(yesterday);
    }

    private boolean compareExchangeRates(ExchangeRate todayRate, ExchangeRate yesterdayRate) {
        Double todayValue = todayRate.getRates().get(currency);
        Double yesterdayValue = yesterdayRate.getRates().get(currency);

        logger.debug("todayValue = " + todayValue + " " + currency);
        logger.debug("yesterdayValue = " + yesterdayValue + " " + currency);

        return todayValue > yesterdayValue;
    }
}