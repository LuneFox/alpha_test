package ru.lunefox.alphatest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class RootController {
    @GetMapping
    public String getRoot() {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot see this page.");
    }
}