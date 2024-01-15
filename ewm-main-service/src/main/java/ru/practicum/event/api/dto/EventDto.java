package ru.practicum.event.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import prototype.Constants;
import ru.practicum.category.api.dto.CategoryDto;
import ru.practicum.location.entity.Location;
import ru.practicum.user.api.dto.UserShortDto;

import static prototype.Constants.DATE_FORMAT;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private long id;
    private @NotNull @Size(
            max = 256
    ) String title;
    private @NotNull @Size(
            max = 512,
            min = 64
    ) String annotation;
    private @Size(
            max = 2048
    ) String description;
    private @NotNull CategoryDto category;
    private @NotNull UserShortDto initiator;
    private @NotNull Location location;
    @BooleanFlag
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
    private long confirmedRequests;
    private @PositiveOrZero int participantLimit;
    @BooleanFlag
    private boolean requestModeration;
    private Constants.EventState state;
    private long views;
}
