package ru.practicum.event.request.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import prototype.Constants.RequestState;
import ru.practicum.event.entity.Event;
import ru.practicum.user.entity.User;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "event_id")
//    Warning:(33, 25) Cannot resolve column 'event_id'
    private Event event;
    @ManyToOne
    @JoinColumn(name = "requester_id")
//    Warning:(36, 25) Cannot resolve column 'requester_id'
    private User requester;
    private LocalDateTime created;
    @Enumerated(EnumType.STRING)
    private RequestState status;
}
