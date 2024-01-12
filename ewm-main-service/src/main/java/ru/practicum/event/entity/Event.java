package ru.practicum.event.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import prototype.Constants;
import ru.practicum.category.entity.Category;
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
            max = 256
    ) String title;
    private @NotNull @Size(
            max = 512,
            min = 64
    ) String annotation;
    private @Size(
            max = 2048
    ) String description;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private @NotNull Category category;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private @NotNull User initiator;

    @OneToOne
    @JoinColumn(name = "location_id")
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
    private Constants.EventState state;
    private long views;
}
