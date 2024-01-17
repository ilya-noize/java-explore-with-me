package ru.practicum.exception.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

import static prototype.Constants.DATE_FORMAT;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private String description;
    private List<String> errors;
    private String message;
    private String reason;
    private HttpStatus status;
    @JsonFormat(
            pattern = DATE_FORMAT
    )
    private LocalDateTime timestamp;
}