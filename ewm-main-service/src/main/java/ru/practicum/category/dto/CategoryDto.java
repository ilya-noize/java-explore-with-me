package ru.practicum.category.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CategoryDto {
    private Long id;
    private @NotNull
    @Size(
            max = 50,
            min = 1
    ) String name;

    public CategoryDto() {
    }
}
