package ru.lunefox.alphatest.model.rates;

import feign.Feign;
import feign.Logger;
import feign.RequestLine;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Setter
@Component
@Scope("singleton")
@PropertySource("classpath:general.properties")
public class ExchangeRateService {

    private interface ExchangeRateClient {
        @RequestLine("GET")
        ExchangeRate find();
    }

    @Value("${rates.server}")
    private String server;

    @Value("${rates.app_id}")
    private String appId;

    @Value("${rates.relative_currency}")
    private String base;

    public ExchangeRate getExchangeRate(String request) {
        final String target = server + request + "?app_id=" + appId + "&base=" + base;

        ExchangeRateClient client = Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(ExchangeRateClient.class))
                .logLevel(Logger.Level.FULL)
                .target(ExchangeRateClient.class, target);

        return client.find();
    }
}