package com.eventLedger.gateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountServiceUnavailableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Map<String, String> handleAccountServiceDown(
            AccountServiceUnavailableException ex) {

        Map<String, String> response = new HashMap<>();

        response.put("error", ex.getMessage());

        return response;
    }

    @ExceptionHandler(EventNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(
            EventNotFoundException ex) {

        Map<String, String> response =
                new HashMap<>();

        response.put("error", ex.getMessage());

        return response;
    }
}
