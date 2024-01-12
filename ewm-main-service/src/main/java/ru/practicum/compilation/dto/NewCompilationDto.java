package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {
    private List<Long> events;
    private boolean pinned;
    private @NotNull String title;
}
