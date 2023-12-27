package ru.practicum.dto.stats.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Builder
@Getter
public class RequestUserUrlDto {
    private int id;
    @Size(max = 64)
    private String app;
    @Size(max = 255)
    private String uri;
    @Size(max = 39)
    private String ip;
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
