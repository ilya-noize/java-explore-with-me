package ru.practicum.event.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.category.api.dto.CategoryDto;
import ru.practicum.user.api.dto.UserShortDto;

import java.time.LocalDateTime;

import static ru.practicum.constants.Constants.DATE_FORMAT;

/**
 * Just a class with data. Don't touch him.
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {
    private long id;
    private String title;
    private String annotation;
    private CategoryDto category;
    private UserShortDto initiator;
    private long confirmedRequests;
    @JsonFormat(
            pattern = DATE_FORMAT
    )
    private LocalDateTime eventDate;
    private boolean paid;
    private long views;
}
