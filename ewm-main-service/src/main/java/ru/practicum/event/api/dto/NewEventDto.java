package ru.practicum.event.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.location.dto.LocationDto;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.constants.Constants.DATE_FORMAT;
import static ru.practicum.constants.Constants.MAX_EVENT_ANNOTATION_LENGTH;
import static ru.practicum.constants.Constants.MAX_EVENT_DESCRIPTION_LENGTH;
import static ru.practicum.constants.Constants.MAX_EVENT_TITLE_LENGTH;
import static ru.practicum.constants.Constants.MIN_EVENT_ANNOTATION_LENGTH;
import static ru.practicum.constants.Constants.MIN_EVENT_DESCRIPTION_LENGTH;
import static ru.practicum.constants.Constants.MIN_EVENT_TITLE_LENGTH;

/**
 * <h2>Новое событие</h2>
 * {@link #title}               Заголовок события <br/>
 * {@link #annotation}          Краткое описание события <br/>
 * {@link #description}         Полное описание события <br/>
 * {@link #category}            id категории к которой относится событие <br/>
 * {@link #location}            Широта и долгота места проведения события <br/>
 * {@link #paid}                Нужно ли оплачивать участие в событии. default: false<br/>
 * {@link #eventDate}           Дата и время на которые намечено событие. <br/>
 * {@link #participantLimit}    Ограничение на количество участников. 0 (default) - означает отсутствие ограничения <br/>
 * {@link #requestModeration}   Нужна ли пре-модерация заявок на участие (true(default) - manual, false - auto). <br/>
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    private @NotBlank @Size(
            max = MAX_EVENT_TITLE_LENGTH,
            min = MIN_EVENT_TITLE_LENGTH
    ) String title;
    private @NotBlank @Size(
            max = MAX_EVENT_ANNOTATION_LENGTH,
            min = MIN_EVENT_ANNOTATION_LENGTH
    ) String annotation;
    private @NotBlank @Size(
            max = MAX_EVENT_DESCRIPTION_LENGTH,
            min = MIN_EVENT_DESCRIPTION_LENGTH
    ) String description;
    private @NotNull Long category;
    private @NotNull LocationDto location;
    private Boolean paid;
    @JsonFormat(
            pattern = DATE_FORMAT
    )
    private @NotNull @Future LocalDateTime eventDate;
    private @PositiveOrZero Integer participantLimit;
    private Boolean requestModeration;
}
