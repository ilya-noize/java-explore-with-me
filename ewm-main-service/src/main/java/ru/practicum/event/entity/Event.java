package ru.practicum.event.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.category.entity.Category;
import ru.practicum.event.api.service.EventService;
import ru.practicum.location.entity.Location;
import ru.practicum.user.entity.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static prototype.Constants.DATE_FORMAT;

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
    @BooleanFlag
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
    private long confirmedRequests;
    private @PositiveOrZero int participantLimit;
    @BooleanFlag
    private boolean requestModeration;
    private EventService.EventState state;
    private long views;
}
