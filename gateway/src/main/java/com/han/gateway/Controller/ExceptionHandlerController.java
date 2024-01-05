package com.han.gateway.Controller;

import com.han.gateway.exception.ServiceUnavailableException;
import com.han.gateway.payload.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ServiceUnavailableException.class)
    public ExceptionResponse handleServiceUnavailableException(ServiceUnavailableException exception){
        return new ExceptionResponse(exception.getMessage(), HttpStatus.SERVICE_UNAVAILABLE.value());
    }
}
