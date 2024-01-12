package ru.practicum.exception.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

import static prototype.Constants.DATE_FORMAT;

public class ApiError {
    private String description;
    private List<String> errors;
    private String message;
    private String reason;
    private String status;
    @JsonFormat(
            pattern = DATE_FORMAT
    )
    private LocalDateTime timestamp;

    public ApiError() {
    }
}