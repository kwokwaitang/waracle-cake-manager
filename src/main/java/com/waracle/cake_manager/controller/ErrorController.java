package com.waracle.cake_manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/error")
    public String error(Model model) {
        return "error";
    }

    @GetMapping("/throw-an-exception")
    public String exceptionHandling() throws Exception {
        throw new Exception("Deliberately throwing an exception");
    }
}
