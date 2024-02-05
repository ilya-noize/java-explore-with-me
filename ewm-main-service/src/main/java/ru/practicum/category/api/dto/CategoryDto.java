package ru.practicum.category.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static ru.practicum.constants.Constants.MAX_CATEGORY_NAME_LENGTH;
import static ru.practicum.constants.Constants.MIN_CATEGORY_NAME_LENGTH;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    private @NotNull
    @Size(
            max = MAX_CATEGORY_NAME_LENGTH,
            min = MIN_CATEGORY_NAME_LENGTH
    ) String name;
}
