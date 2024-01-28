package ru.practicum.exception.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.entity.ApiError;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    private void logError(HttpStatus status, String message, Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTraceString = sw.toString().replace(", ", "\n");

        log.error("[!] Received the status {} Error: {}\n{}", status, message, stackTraceString);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleNotValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();

        List<ObjectError> allErrors = result.getAllErrors();
        List<String> errors = allErrors.stream()
                .map(ObjectError::toString)
                .collect(toList());

        logError(BAD_REQUEST, allErrors.get(0).getDefaultMessage(), e);

        return ApiError.builder()
                .description("Method Argument Not Valid")
                .errors(errors)
                .message(allErrors.get(0).getDefaultMessage())
                .reason(requireNonNull(result.getFieldError()).getField())
                .status(BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ApiError handleNotFoundException(NotFoundException e) {
        String message = e.getMessage();

        logError(NOT_FOUND, message, e);

        return ApiError.builder()
                .description("Not found object.")
                .errors(Collections.emptyList())
                .message(message)
                .reason("Object not found.")
                .status(NOT_FOUND)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(CONFLICT)
    public ApiError handleConflictException(ConflictException e) {
        String message = e.getMessage();

        logError(CONFLICT, message, e);

        return ApiError.builder()
                .description("Data integrity violation.")
                .errors(Collections.emptyList())
                .message(message)
                .reason("Data integrity violation.")
                .status(CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleBadRequestException(BadRequestException e) {
        String message = e.getMessage();

        logError(BAD_REQUEST, message, e);

        return ApiError.builder()
                .description("Wrong request.")
                .errors(Collections.emptyList())
                .message(message)
                .reason("Wrong request.")
                .status(BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(FORBIDDEN)
    public ApiError handleForbiddenException(ForbiddenException e) {
        String message = e.getMessage();

        logError(FORBIDDEN, message, e);

        return ApiError.builder()
                .description("Access is denied.")
                .errors(Collections.emptyList())
                .message(message)
                .reason("Access is denied.")
                .status(FORBIDDEN)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApiError handleNullPointerException(NullPointerException e) {
        StackTraceElement[] allErrors = e.getStackTrace();

        logError(INTERNAL_SERVER_ERROR, allErrors[0].toString(), e);

        return ApiError.builder()
                .description("Null Pointer")
                .errors(Collections.singletonList(Arrays.toString(allErrors)))
                .message(e.getMessage())
                .reason(e.getClass().getName())
                .status(INTERNAL_SERVER_ERROR)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApiError handleIllegalArgumentException(IllegalArgumentException e) {
        StackTraceElement[] allErrors = e.getStackTrace();

        logError(INTERNAL_SERVER_ERROR, allErrors[0].toString(), e);

        return ApiError.builder()
                .description("Illegal Argument")
                .errors(Collections.singletonList(Arrays.toString(allErrors)))
                .message(e.getMessage())
                .reason(e.getClass().getName())
                .status(INTERNAL_SERVER_ERROR)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
