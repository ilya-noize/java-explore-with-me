package ru.practicum.event.request.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.event.request.api.service.EventRequestService.RequestState;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDto {
    private @NotNull long id;
    private @NotNull LocalDateTime created;
    private @NotNull long event;
    private @NotNull long requester;
    private @NotNull RequestState status;
}
