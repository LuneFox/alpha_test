package ru.lunefox.alphatest.model.gifs;

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
public class GifService {

    public interface GifClient {
        @RequestLine("GET")
        Gif find();
    }

    @Value("${giphy.server}")
    private String server;

    @Value("${giphy.api_key}")
    private String apiKey;

    @Value("${giphy.rating}")
    private String rating;

    @Value("${giphy.rich_tag}")
    private String richTag;

    @Value("${giphy.broke_tag}")
    private String brokeTag;


    public Gif getGif(Gif.Tag tag) {
        String searchTag = (tag == Gif.Tag.RICH) ? richTag : brokeTag;
        final String target = server + "?api_key=" + apiKey + "&tag=" + searchTag + "&rating=" + rating;

        GifClient client = Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(GifClient.class))
                .logLevel(Logger.Level.FULL)
                .target(GifClient.class, target);

        return client.find();
    }
}