package ru.practicum.event.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.event.entity.StateAdminAction;
import ru.practicum.location.dto.LocationDto;

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
 * <h2>Обновлённое событие</h2>
 * {@link #title}               Заголовок события <br/>
 * {@link #annotation}          Краткое описание события <br/>
 * {@link #description}         Полное описание события <br/>
 * {@link #category}            id категории к которой относится событие <br/>
 * {@link #location}            Широта и долгота места проведения события <br/>
 * {@link #paid}                Нужно ли оплачивать участие в событии. default: false<br/>
 * {@link #eventDate}           Дата и время на которые намечено событие. <br/>
 * {@link #participantLimit}    Ограничение на количество участников. 0 (default) - означает отсутствие ограничения <br/>
 * {@link #requestModeration}   Нужна ли пре-модерация заявок на участие (true(default) - manual, false - auto). <br/>
 * {@link #stateAction}         Изменение состояния события
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventAdminDto {
    private @Size(
            max = MAX_EVENT_TITLE_LENGTH,
            min = MIN_EVENT_TITLE_LENGTH
    ) String title;
    private @Size(
            max = MAX_EVENT_ANNOTATION_LENGTH,
            min = MIN_EVENT_ANNOTATION_LENGTH
    ) String annotation;
    private @Size(
            max = MAX_EVENT_DESCRIPTION_LENGTH,
            min = MIN_EVENT_DESCRIPTION_LENGTH
    ) String description;
    private Long category;
    private LocationDto location;
    private Boolean paid;
    @JsonFormat(
            pattern = DATE_FORMAT
    )
    private LocalDateTime eventDate;
    private @PositiveOrZero Integer participantLimit;
    private Boolean requestModeration;
    private StateAdminAction stateAction;
}
