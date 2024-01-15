package ru.practicum.event.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.category.api.dto.CategoryDto;
import ru.practicum.user.api.dto.UserShortDto;

import static prototype.Constants.DATE_FORMAT;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {
    private long id;
    private @NotNull @Size(
            max = 256
    ) String title;
    private @NotNull @Size(
            max = 512,
            min = 64
    ) String annotation;
    private @NotNull CategoryDto category;
    private @NotNull UserShortDto initiator;
    private long confirmedRequests;
    @JsonFormat(
            pattern = DATE_FORMAT
    )
    private @NotNull LocalDateTime eventDate;
    @BooleanFlag
    private @NotNull boolean paid;
    private long views;
}
