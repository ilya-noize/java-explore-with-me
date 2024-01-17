package ru.practicum.event.request.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventRequestDto {
    private long eventId;
    private long initializerId;
    private long visitorId;
    private LocalDateTime created;
    private boolean request;
}
