package ru.practicum.event.request.api.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import prototype.Constants;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDto {
    private long id;
    private LocalDateTime created;
    private long event;
    private long requester;
    private Constants.ParticipationRequestState status;
}
