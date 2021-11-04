package com.waracle.cake_manager.controller;

import com.waracle.cake_manager.advice.LogMethodAccess;
import com.waracle.cake_manager.exception.AnotherMyOwnRuntimeException;
import com.waracle.cake_manager.exception.MyOwnRuntimeException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @LogMethodAccess
    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<String> handleConflict(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * See {@link CakeErrorController#myOwnExceptionHandling()} on what HTTP status code is delivered
     *
     * @param exception
     * @return The response body and an HTTP status code
     */
    @LogMethodAccess
    @ExceptionHandler(MyOwnRuntimeException.class)
    public ResponseEntity<String> handleMyOwnRuntimeException(RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Display the exception as a modal when the index page is rendered
     * <p>
     * https://www.baeldung.com/spring-mvc-model-model-map-model-view
     *
     * @param model     Holds information for the view (i.e. to provide a view with usable data)
     * @param exception Contains the message detailing the issue that caused the exception
     * @return The view
     */
    @LogMethodAccess
    @ExceptionHandler(AnotherMyOwnRuntimeException.class)
    public String handleAnotherMyOwnRuntimeException(Model model, Exception exception) {
        model.addAttribute("exceptionIssue", exception.getMessage());

        return "index";
    }
}
