package ru.lunefox.alphatest.model.rates;

import lombok.Data;

import java.util.Map;

@Data
public class ExchangeRate {
    private long timestamp;
    private String base;
    private Map<String, Double> rates;
}
