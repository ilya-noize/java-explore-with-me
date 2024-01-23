package ru.practicum.category.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static ru.practicum.constants.Constants.MAX_CATEGORY_NAME_LENGTH;
import static ru.practicum.constants.Constants.MIN_CATEGORY_NAME_LENGTH;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCategoryDto {
    private @NotBlank @Size(
            max = MAX_CATEGORY_NAME_LENGTH,
            min = MIN_CATEGORY_NAME_LENGTH
    ) String name;
}
