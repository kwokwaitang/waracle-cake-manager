package com.waracle.cake_manager.v1update;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
}
