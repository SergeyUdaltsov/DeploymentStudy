package com.study.deployment.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyController.class);

    @PostMapping("/hello")
    public String hello(@RequestBody String message) {
        LOGGER.info("Received payload: {}", message);
        return "Hello from my controller";
    }
}
