package com.waracle.cake_manager.controller;

import com.waracle.cake_manager.exception.MyOwnException;
import com.waracle.cake_manager.exception.MyOwnRuntimeException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CakeErrorController implements ErrorController {

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

    /**
     * This exception will generate a redirect to /error which is handled by
     * {@link CakeErrorController#error(HttpServletRequest)}
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/throw-an-exception")
    public String exceptionHandling() throws MyOwnException /* Checked - so must be either handled or declared */ {
        throw new MyOwnException("Deliberately throwing an exception & handling it");
    }

    /**
     * This custom runtime exception will be caught by
     * {@link com.waracle.cake_manager.controller.GlobalControllerExceptionHandler#handleMyOwnRuntimeException(RuntimeException)}
     *
     * @return
     * @throws RuntimeException
     */
    @GetMapping("/throw-my-own-exception")
    public String myOwnExceptionHandling() throws MyOwnRuntimeException {
        throw new MyOwnRuntimeException("Deliberately throwing my own run-time exception & handling it");
    }
}
