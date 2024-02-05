package ru.practicum.user.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static ru.practicum.constants.Constants.MAX_USER_EMAIL_LENGTH;
import static ru.practicum.constants.Constants.MAX_USER_NAME_LENGTH;
import static ru.practicum.constants.Constants.MIN_USER_EMAIL_LENGTH;
import static ru.practicum.constants.Constants.MIN_USER_NAME_LENGTH;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewUserDto {
    @Size(
            max = MAX_USER_EMAIL_LENGTH,
            min = MIN_USER_EMAIL_LENGTH
    )
    private @NotNull @Email String email;
    @Size(
            max = MAX_USER_NAME_LENGTH,
            min = MIN_USER_NAME_LENGTH
    )
    private @NotNull @NotBlank String name;
}
