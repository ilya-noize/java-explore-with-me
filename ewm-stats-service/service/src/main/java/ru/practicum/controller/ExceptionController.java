package ru.practicum.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.DateTimeException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(DateTimeException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<?> handleThrowable(DateTimeException e) {
        String message = e.getMessage();

        return ResponseEntity.status(BAD_REQUEST)
                .body(message);
    }
}
