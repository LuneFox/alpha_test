package ru.lunefox.alphatest.model.gifs;

import lombok.Data;

import java.util.Map;

@Data
public class Gif {
    private Map<String, Object> data;

    public enum Tag {
        RICH, BROKE
    }

    public String getEmbedUrl() {
        return (String) data.get("embed_url");
    }
}