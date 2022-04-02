package com.waracle.cake_manager.v1update;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.logging.Logger;

@RestController
public class TestController {

    private static final Logger LOGGER = Logger.getGlobal();

    @PostMapping("/v1/update")
    @ResponseStatus(HttpStatus.CREATED)
    public void v1Update(@Valid @RequestBody Body body) {
        LOGGER.info("Running v1Update()");
    }

    @GetMapping("/v1/body")
    @ResponseStatus(HttpStatus.OK)
    public void v1Body(Body body) {
        LOGGER.info("Running v1UBody()");
    }
}
