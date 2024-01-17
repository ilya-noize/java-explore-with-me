package ru.practicum.event.request.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prototype.Constants;
import ru.practicum.event.request.entity.EventRequest;

import java.util.List;

public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {
    List<EventRequest> getByEvent_Id(Long id);

    long countByEvent_IdAndStatus(Long id, Constants.RequestState status);
}
