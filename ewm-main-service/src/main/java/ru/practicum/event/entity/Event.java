package ru.practicum.event.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.category.entity.Category;
import ru.practicum.event.api.service.EventService;
import ru.practicum.event.request.entity.EventRequest;
import ru.practicum.location.entity.Location;
import ru.practicum.user.entity.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.constants.Constants.DATE_FORMAT;

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
    @ManyToOne
    @JoinColumn(name = "category_id")
//    Warning:(53, 25) Cannot resolve column 'category_id'
    private @NotNull Category category;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
//    Warning:(56, 25) Cannot resolve column 'initiator_id'
    private @NotNull User initiator;
    @OneToOne
    @JoinColumn(name = "location_id")
//    Warning:(60, 25) Cannot resolve column 'location_id'
    private @NotNull Location location;
    private @NotNull boolean paid;
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
    private @NotNull LocalDateTime eventDate;
    @OneToMany(mappedBy = "event")
    private List<EventRequest> confirmedRequests;
    private @PositiveOrZero int participantLimit;
    private boolean requestModeration;
    private EventService.EventState state;
    private long views;
}
