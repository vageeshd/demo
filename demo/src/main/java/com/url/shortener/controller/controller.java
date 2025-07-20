package com.url.shortener.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class controller {
    @GetMapping("/b")
    public String hello() {
        return "Hello, Spring!";
    }
}
