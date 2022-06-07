package ru.lunefox.alphatest.model.rates;

import lombok.Data;

import java.util.Map;

@Data
public class ExchangeRate {
    private long timestamp; // UNIX UTC Seconds
    private String base;
    private Map<String, Double> rates;

    public boolean containsCurrency(String currency) {
        return rates.containsKey(currency);
    }
}