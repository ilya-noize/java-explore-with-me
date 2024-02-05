package ru.practicum.moderate.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.event.entity.Event;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.constants.Constants.MAX_MODERATE_COMMENT_LENGTH;
import static ru.practicum.constants.Constants.MAX_MODERATE_REASON_LENGTH;
import static ru.practicum.constants.Constants.MIN_MODERATE_COMMENT_LENGTH;
import static ru.practicum.constants.Constants.MIN_MODERATE_REASON_LENGTH;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;
    private LocalDateTime created;
    @Enumerated(EnumType.STRING)
    private ReviewState state;
    @Size(
            min = MIN_MODERATE_REASON_LENGTH,
            max = MAX_MODERATE_REASON_LENGTH
    )
    private String reason;
    @Size(
            min = MIN_MODERATE_COMMENT_LENGTH,
            max = MAX_MODERATE_COMMENT_LENGTH
    )
    private String comment;
}

