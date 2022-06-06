package ru.lunefox.alphatest.model.rates;

import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
@PropertySource("classpath:general.properties")
public class ExchangeRateClientBuilder {

    @Value("${rates.server}")
    private String server;

    @Value("${rates.app_id}")
    private String appId;

    @Value("${rates.relative_currency}")
    private String base;

    public ExchangeRateClient build(String request) {
        final String target = server + request + "?app_id=" + appId + "&base=" + base;

        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(ExchangeRateClient.class))
                .logLevel(Logger.Level.FULL)
                .target(ExchangeRateClient.class, target);
    }
}