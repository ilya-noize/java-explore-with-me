package ru.practicum.event.api.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.entity.Event;
import ru.practicum.user.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends CrudRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    @Transactional
    @Modifying
    @Query("update Event e set e.confirmedRequests = :confirmedRequests where e.id = :id")
    void updateConfirmedRequestsById(@Param("confirmedRequests") long confirmedRequests,
                                     @Param("id") long eventId);

    @Transactional
    @Modifying
    @Query("update Event e set e.views = :views where e.id = :id")
    int updateViewsById(@Param("views") long views,
                        @Param("id") long eventId);

    Optional<List<Event>> getByInitiator(User initiator, Pageable pageable);

    boolean existsByIdAndInitiator_Id(long eventId, long userId);

    Optional<Event> getByInitiatorAndId(User user, long eventId);

    /**
     * Used in category service. delete-method.
     *
     * @param categoryId ID category
     * @return Count events on category
     */
    long countByCategory_Id(long categoryId);

    Optional<List<Event>> getByIdInOrderByIdAsc(Collection<Long> eventIds);

    List<Event> findAll(Pageable pageable);
}
