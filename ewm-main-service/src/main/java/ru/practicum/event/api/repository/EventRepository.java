package ru.practicum.event.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.entity.Event;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    long countByCategory_Id(Long id);

    Optional<List<Event>> getByIdInOrderByIdAsc(Collection<Long> ids);
}
