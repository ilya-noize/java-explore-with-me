package ru.practicum.event.request.api.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.entity.RequestState;
import ru.practicum.event.request.entity.EventRequest;

import java.util.List;
import java.util.Optional;

public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {
    boolean existsByRequester_IdAndEvent_Id(Long userId, Long eventId);

    Optional<List<EventRequest>> findByRequester_Id(Long requesterId, Pageable pageable);

    List<EventRequest> getByEvent_Id(Long eventId);

    int countByEvent_IdAndStatus(Long eventId, RequestState status);
}
