package ru.practicum.event.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.category.api.dto.CategoryDto;
import ru.practicum.event.entity.EventState;
import ru.practicum.location.entity.Location;
import ru.practicum.moderate.api.dto.ReviewDto;
import ru.practicum.user.api.dto.UserShortDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.constants.Constants.DATE_FORMAT;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class EventDto {
    private long id;
    private @NotNull String title;
    private @NotNull String annotation;
    private String description;
    private @NotNull CategoryDto category;
    private @NotNull UserShortDto initiator;
    private @NotNull Location location;
    private @NotNull boolean paid;
    @JsonFormat(
            pattern = DATE_FORMAT
    )
    private LocalDateTime createdOn;
    @JsonFormat(
            pattern = DATE_FORMAT
    )
    private LocalDateTime publishedOn;
    @JsonFormat(
            pattern = DATE_FORMAT
    )
    private @NotNull LocalDateTime eventDate;
    private int confirmedRequests;
    private @PositiveOrZero int participantLimit;
    private boolean requestModeration;
    private EventState state;
    private long views;
    private List<ReviewDto> reviews;
}
