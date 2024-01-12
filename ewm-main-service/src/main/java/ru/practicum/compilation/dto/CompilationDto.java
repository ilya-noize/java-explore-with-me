package ru.practicum.compilation.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    private @NotNull @Positive long id;
    @BooleanFlag
    private boolean pinned;
    private @NotNull @Size(
            max = 128,
            min = 3
    ) String title;
}