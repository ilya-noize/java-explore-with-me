package ru.practicum.compilation.api.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    private @NotNull @Positive long id;
    @BooleanFlag
    private Boolean pinned;
    private @NotNull @Size(
            max = 128,
            min = 3
    ) String title;
    private List<Long> events;
}