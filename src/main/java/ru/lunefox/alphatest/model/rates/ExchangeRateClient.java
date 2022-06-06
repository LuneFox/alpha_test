package ru.lunefox.alphatest.model.rates;

import feign.RequestLine;

public interface ExchangeRateClient {
    @RequestLine("GET")
    ExchangeRate find();
}

