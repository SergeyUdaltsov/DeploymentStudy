package com.study.deployment.controller;

import com.study.deployment.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyController.class);

    private final S3Service s3Service;

    @PostMapping("/hello")
    public String hello(@RequestBody String message) {
        LOGGER.info("Received payload: {}", message);

        s3Service.savePayloadToBucket(message);
        LOGGER.info("Payload successfully stored to S3");

        return "Hello from my controller";
    }
}
