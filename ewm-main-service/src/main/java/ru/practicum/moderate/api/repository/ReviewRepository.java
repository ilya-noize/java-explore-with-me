package ru.practicum.moderate.api.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.moderate.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends CrudRepository<Review, Long>, JpaSpecificationExecutor<Review> {
    List<Review> findByEvent_Id(Long eventId, Sort sort);

    Optional<Review> findByIdAndEvent_Id(Long reviewId, Long eventId);
}