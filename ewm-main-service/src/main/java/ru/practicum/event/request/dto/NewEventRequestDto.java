package ru.practicum.event.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventRequestDto {
    private Long eventId;
    private Long organizerId;
    private Long visitorId;
    private boolean request;
}
