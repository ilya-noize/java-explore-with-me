package ru.practicum.event.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.category.api.dto.CategoryDto;
import ru.practicum.location.entity.Location;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static prototype.Constants.DATE_FORMAT;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
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
    private @NotNull Location location;
    @BooleanFlag
    private @NotNull boolean paid;
    @JsonFormat(
            pattern = DATE_FORMAT
    )
    private @NotNull LocalDateTime eventDate;
    private @PositiveOrZero int participantLimit;
    @BooleanFlag
    private boolean requestModeration;
}
