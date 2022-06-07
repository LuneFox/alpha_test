package ru.lunefox.alphatest.model.gifs;

import feign.RequestLine;

public interface GifClient {
    @RequestLine("GET")
    Gif find();
}