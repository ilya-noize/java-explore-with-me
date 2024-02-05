package ru.practicum.compilation.api.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.event.api.dto.EventShortDto;

import java.util.List;

import static ru.practicum.constants.Constants.MAX_COMPILATION_TITLE_LENGTH;
import static ru.practicum.constants.Constants.MIN_COMPILATION_TITLE_LENGTH;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    private @NotNull @Positive long id;
    private @NotNull Boolean pinned;
    private @NotNull @Size(
            max = MAX_COMPILATION_TITLE_LENGTH,
            min = MIN_COMPILATION_TITLE_LENGTH
    ) String title;
    private List<EventShortDto> events;
}