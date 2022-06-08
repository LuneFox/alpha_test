package ru.lunefox.alphatest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import ru.lunefox.alphatest.model.gifs.Gif;
import ru.lunefox.alphatest.model.gifs.GifService;
import ru.lunefox.alphatest.model.rates.ExchangeRateService;
import ru.lunefox.alphatest.model.rates.ExchangeRateHistoryAnalyzer;


@Controller
public class RatesController {
    private final GifService gifService;
    private final ExchangeRateHistoryAnalyzer historyAnalyzer;

    @Autowired
    public RatesController(ExchangeRateService exchangeRateService, GifService gifService) {
        this.gifService = gifService;
        this.historyAnalyzer = new ExchangeRateHistoryAnalyzer(exchangeRateService);
    }

    @GetMapping
    public String getRoot() {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot see this page.");
    }

    @GetMapping("/rates")
    public String getRateForCurrency(@RequestParam(value = "currency", required = false)
                                     String currency,
                                     Model model) {
        if (currency == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parameter 'currency' is not present.");
        }

        boolean rich = historyAnalyzer.isRateTodayHigherThanYesterday(currency);
        Gif gif = gifService.getGif(rich ? Gif.Tag.RICH : Gif.Tag.BROKE);
        model.addAttribute("embed_url", gif.getEmbedUrl());
        return "gif";
    }
}