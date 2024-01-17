package ru.practicum.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String error) {
        super(error);
    }
}
