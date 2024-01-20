package ru.practicum.event.api.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.event.entity.Event;
import ru.practicum.user.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends CrudRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    Optional<List<Event>> getByInitiator(User initiator, Pageable pageable);

    boolean existsByIdAndInitiator_Id(Long id, Long id1);

    Optional<Event> getByInitiatorAndId(User user, Long eventId);

    /**
     * Used in category service. delete-method.
     * @param id ID category
     * @return Count events on category
     */
    long countByCategory_Id(Long id);

    Optional<List<Event>> getByIdInOrderByIdAsc(Collection<Long> ids);

    List<Event> findAll(Pageable pageable);
}
