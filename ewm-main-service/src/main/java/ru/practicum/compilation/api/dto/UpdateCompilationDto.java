package ru.practicum.compilation.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

import static ru.practicum.constants.Constants.MAX_COMPILATION_NAME_LENGTH;
import static ru.practicum.constants.Constants.MIN_COMPILATION_NAME_LENGTH;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompilationDto {
    private @Size(
            max = MAX_COMPILATION_NAME_LENGTH,
            min = MIN_COMPILATION_NAME_LENGTH
    ) String title;
    private Boolean pinned;
    private List<Long> events;
}
