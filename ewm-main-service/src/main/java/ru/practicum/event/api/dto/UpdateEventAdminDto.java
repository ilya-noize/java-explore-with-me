package ru.practicum.event.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.location.entity.Location;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static prototype.Constants.DATE_FORMAT;

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
    private @NotNull @Size(
            max = 120,
            min = 3
    ) String title;
    private @NotNull @Size(
            max = 2000,
            min = 20
    ) String annotation;
    private @Size(
            max = 7000,
            min = 20
    ) String description;
    private @NotNull Long category;
    private @NotNull Location location;
    @BooleanFlag
    private @NotNull Boolean paid;
    @JsonFormat(
            pattern = DATE_FORMAT
    )
    private @NotNull LocalDateTime eventDate;
    private @PositiveOrZero Integer participantLimit;
    @BooleanFlag
    private Boolean requestModeration;
    private String stateAction;
}
