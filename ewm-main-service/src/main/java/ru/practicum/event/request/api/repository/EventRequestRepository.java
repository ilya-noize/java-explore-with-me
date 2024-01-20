package ru.practicum.event.request.api.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.entity.Event;
import ru.practicum.event.request.api.service.EventRequestService;
import ru.practicum.event.request.entity.EventRequest;
import ru.practicum.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {
    @Transactional
    @Modifying
    @Query("update EventRequest e set e.status = :status " +
            "where e.id = :id and e.event = :event and e.requester = :requester")
    void eventRequestCancel(
            @Param("status") EventRequestService.RequestState status,
            @Param("id") Long id,
            @Param("event") Event event,
            @Param("requester") User requester);

    Optional<List<EventRequest>> findByRequester_Id(Long requesterId, Pageable pageable);

    List<EventRequest> getByEvent_Id(Long eventId);

    int countByEvent_IdAndStatus(Long eventId, EventRequestService.RequestState status);
}
