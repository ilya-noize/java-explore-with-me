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

        String defaultMessage = allErrors.get(0).getDefaultMessage();
        logError(BAD_REQUEST, defaultMessage, e);

        return ApiError.builder()
                .description("Method Argument Not Valid")
                .errors(errors)
                .message(defaultMessage)
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

        String shortDescription = "Object not found.";
        return ApiError.builder()
                .description(shortDescription)
                .errors(Collections.emptyList())
                .message(message)
                .reason(shortDescription)
                .status(NOT_FOUND)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(CONFLICT)
    public ApiError handleConflictException(ConflictException e) {
        String message = e.getMessage();

        logError(CONFLICT, message, e);

        String shortDescription = "Data integrity violation.";
        return ApiError.builder()
                .description(shortDescription)
                .errors(Collections.emptyList())
                .message(message)
                .reason(shortDescription)
                .status(CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleBadRequestException(BadRequestException e) {
        String message = e.getMessage();

        logError(BAD_REQUEST, message, e);

        String shortDescription = "Wrong request.";
        return ApiError.builder()
                .description(shortDescription)
                .errors(Collections.emptyList())
                .message(message)
                .reason(shortDescription)
                .status(BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(FORBIDDEN)
    public ApiError handleForbiddenException(ForbiddenException e) {
        String message = e.getMessage();

        logError(FORBIDDEN, message, e);

        String shortDescription = "Access is denied.";
        return ApiError.builder()
                .description(shortDescription)
                .errors(Collections.emptyList())
                .message(message)
                .reason(shortDescription)
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

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(Throwable e) {
        StackTraceElement[] allErrors = e.getStackTrace();

        logError(INTERNAL_SERVER_ERROR, allErrors[0].toString(), e);

        return ApiError.builder()
                .description("In some rare cases it is acceptable to throw Throwables")
                .errors(Collections.singletonList(Arrays.toString(allErrors)))
                .message(e.getMessage())
                .reason(e.getClass().getName())
                .status(INTERNAL_SERVER_ERROR)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
