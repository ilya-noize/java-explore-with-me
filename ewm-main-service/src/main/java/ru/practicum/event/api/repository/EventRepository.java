package ru.practicum.event.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.entity.Event;
import ru.practicum.user.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> getByInitiatorAndId(User user, Long eventId);

    long countByCategory_Id(Long id);

    Optional<List<Event>> getByIdInOrderByIdAsc(Collection<Long> ids);
}
