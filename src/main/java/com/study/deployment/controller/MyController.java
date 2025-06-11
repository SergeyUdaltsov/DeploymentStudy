package com.study.deployment.controller;

import com.study.deployment.service.MessageProcessingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyController.class);

    private final MessageProcessingService processingService;

    @PostMapping("/hello")
    public String hello(@RequestBody String message) {
        LOGGER.info("Received payload: {}", message);

        processingService.processMessage(message);

        return "Payload was processed successfully";
    }
}
