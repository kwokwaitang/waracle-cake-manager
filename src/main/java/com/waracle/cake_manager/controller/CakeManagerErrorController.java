package com.waracle.cake_manager.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
class CakeErrorController implements ErrorController {

    @GetMapping("/error")
    public String error(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "errors/error-404";
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                return "errors/error-401";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "errors/error-500";
            }
        }

        return "errors/error";
    }

    @GetMapping("/throw-an-exception")
    public String exceptionHandling() throws Exception {
        throw new Exception("Deliberately throwing an exception");
    }
}
