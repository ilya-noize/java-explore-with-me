package ru.practicum.event.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.category.entity.Category;
import ru.practicum.constants.Constants;
import ru.practicum.location.entity.Location;
import ru.practicum.user.entity.User;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
 * <h2>Событие</h2>
 * {@link #id} - ID <br/>
 * {@link #title} - Название <br/>
 * {@link #annotation} - Аннотация <br/>
 * {@link #description} - Описание <br/>
 * {@link #category} - Категория <br/>
 * {@link #initiator} - Инициатор (организатор) <br/>
 * {@link #location} - Место проведения (широта, долгота) <br/>
 * {@link #paid} - Триггер необходимости оплаты <br/>
 * {@link #createdOn} - Дата создания (назначается при создании) <br/>
 * {@link #publishedOn} - Дата публикации (назначается после модерации администратором) <br/>
 * {@link #eventDate} - Дата проведения мероприятия <br/>
 * {@link #confirmedRequests} - Список желающий принять участие в мероприятии <br/>
 * {@link #participantLimit} - Максимальное количество участников <br/>
 * {@link #requestModeration} - Триггер модерации инициатором заявок на участие в мероприятии <br/>
 * {@link #state} - Состояние (для модерации) <br/>
 * {@link #views} - Просмотры <br/>
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @OneToOne
    @JoinColumn(name = "location_id")
    private Location location;
    private boolean paid;
    @JsonFormat(
            pattern = DATE_FORMAT
    )
    private LocalDateTime createdOn;
    @JsonFormat(
            pattern = DATE_FORMAT
    )
    private LocalDateTime publishedOn;
    @JsonFormat(
            pattern = DATE_FORMAT
    )
    private LocalDateTime eventDate;
    private int confirmedRequests;
    private @PositiveOrZero int participantLimit;
    private boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private Constants.EventState state;
    private long views;
}
