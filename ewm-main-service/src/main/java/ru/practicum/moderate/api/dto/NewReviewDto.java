package ru.practicum.moderate.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static ru.practicum.constants.Constants.MAX_MODERATE_COMMENT_LENGTH;
import static ru.practicum.constants.Constants.MAX_MODERATE_REASON_LENGTH;
import static ru.practicum.constants.Constants.MIN_MODERATE_COMMENT_LENGTH;
import static ru.practicum.constants.Constants.MIN_MODERATE_REASON_LENGTH;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewReviewDto {
    private Long eventId;
    private @NotNull String state;
    @Size(
            min = MIN_MODERATE_REASON_LENGTH,
            max = MAX_MODERATE_REASON_LENGTH
    )
    private @NotBlank String reason;
    @Size(
            min = MIN_MODERATE_COMMENT_LENGTH,
            max = MAX_MODERATE_COMMENT_LENGTH
    )
    private @NotBlank String comment;
}

