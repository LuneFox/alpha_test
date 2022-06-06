package ru.lunefox.alphatest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.lunefox.alphatest.model.rates.ExchangeRate;
import ru.lunefox.alphatest.model.rates.ExchangeRateClient;
import ru.lunefox.alphatest.model.rates.ExchangeRateClientBuilder;


@RestController
@RequestMapping("/rates")
public class RatesController {

    private ExchangeRateClientBuilder builder;

    @Autowired
    public void setBuilder(ExchangeRateClientBuilder builder) {
        this.builder = builder;
    }

    @GetMapping
    public Object getRates() {
        ExchangeRateClient client = builder.build("latest.json");
        ExchangeRate rate = client.find();
        return rate.getRates();
    }

    @GetMapping("/filter")
    public Object getRateForCurrency(@RequestParam(value = "currency") String currency) {
        ExchangeRateClient client = builder.build("latest.json");
        ExchangeRate rate = client.find();
        return rate.getRates().get(currency.toUpperCase());
    }
}
