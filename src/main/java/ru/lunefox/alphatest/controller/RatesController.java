package ru.lunefox.alphatest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import ru.lunefox.alphatest.model.gifs.Gif;
import ru.lunefox.alphatest.model.gifs.GifClient;
import ru.lunefox.alphatest.model.gifs.GifClientBuilder;
import ru.lunefox.alphatest.model.gifs.Tag;
import ru.lunefox.alphatest.model.rates.ExchangeRateClientBuilder;
import ru.lunefox.alphatest.model.rates.ExchangeRateHistoryAnalyzer;


@Controller
@RequestMapping("/rates")
public class RatesController {

    private ExchangeRateClientBuilder exchangeRateClientBuilder;
    private GifClientBuilder gifClientBuilder;

    @Autowired
    public void setExchangeRateClientBuilder(ExchangeRateClientBuilder exchangeRateClientBuilder,
                                             GifClientBuilder gifClientBuilder) {
        this.exchangeRateClientBuilder = exchangeRateClientBuilder;
        this.gifClientBuilder = gifClientBuilder;
    }

    @GetMapping
    public String getRateForCurrency(@RequestParam(value = "currency", required = false)
                                     String currency,
                                     Model model) {
        if (currency == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parameter 'currency' is not present.");
        }

        boolean rich = isRateTodayHigherThanYesterday(currency);
        Gif gif = getGifAccordingToStatus(rich);
        addGifAsModelAttribute(gif, model);
        return "gif";
    }

    private boolean isRateTodayHigherThanYesterday(String currency) {
        ExchangeRateHistoryAnalyzer analyzer = new ExchangeRateHistoryAnalyzer(exchangeRateClientBuilder);
        return analyzer.isRateTodayHigherThanYesterday(currency);
    }

    private Gif getGifAccordingToStatus(boolean rich) {
        GifClient gifClient = gifClientBuilder.build(rich ? Tag.RICH : Tag.BROKE);
        return gifClient.find();
    }

    private void addGifAsModelAttribute(Gif gif, Model model) {
        String embedUrl = (String) gif.getData().get("embed_url");
        model.addAttribute("embed_url", embedUrl);
    }
}