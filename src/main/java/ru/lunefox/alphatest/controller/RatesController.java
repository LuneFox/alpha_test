package ru.lunefox.alphatest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.lunefox.alphatest.model.rates.ExchangeRateClientBuilder;
import ru.lunefox.alphatest.model.rates.ExchangeRateHistoryAnalyzer;


@RestController
@RequestMapping("/rates")
public class RatesController {

    private ExchangeRateClientBuilder clientBuilder;

    @Autowired
    public void setClientBuilder(ExchangeRateClientBuilder clientBuilder) {
        this.clientBuilder = clientBuilder;
    }

    @GetMapping
    public Object getRateForCurrency(@RequestParam(value = "currency") String currency) {
        ExchangeRateHistoryAnalyzer analyzer = new ExchangeRateHistoryAnalyzer(clientBuilder);

        return analyzer.isRateTodayHigherThanYesterday(currency)
                ? "RICH"
                : "BROKE";
    }
}
