package com.reuven.delayservice.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class DelayController {

    private static final Logger logger = LoggerFactory.getLogger(DelayController.class);

    private static final Duration DELAY_DURATION = Duration.ofSeconds(2);

    @GetMapping("/delay/{sessionId}")
    public String delay(@PathVariable String sessionId) throws InterruptedException {
        Thread.sleep(DELAY_DURATION.toMillis());
        String msg = String.format("SessionId %s. delayed response at %s for %s", sessionId, LocalDateTime.now(), DELAY_DURATION);
        logger.info(msg);
        return msg;
    }
}
