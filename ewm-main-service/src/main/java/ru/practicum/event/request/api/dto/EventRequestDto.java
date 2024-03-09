package ru.practicum.event.request.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.event.entity.RequestState;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static ru.practicum.constants.Constants.DATE_FORMAT;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDto {
    private @NotNull long id;
    @JsonFormat(
            pattern = DATE_FORMAT
    )
    private LocalDateTime created;
    private @NotNull long event;
    private @NotNull long requester;
    private @NotNull RequestState status;
}
