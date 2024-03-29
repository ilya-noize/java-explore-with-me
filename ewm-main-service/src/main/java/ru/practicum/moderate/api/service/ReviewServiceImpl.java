package ru.practicum.moderate.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.event.api.dto.EventDto;
import ru.practicum.event.api.mapper.EventMapper;
import ru.practicum.event.api.repository.EventRepository;
import ru.practicum.event.entity.Event;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.moderate.api.dto.NewReviewDto;
import ru.practicum.moderate.api.dto.ReviewDto;
import ru.practicum.moderate.api.mapper.ReviewMapper;
import ru.practicum.moderate.api.repository.ReviewRepository;
import ru.practicum.moderate.entity.Review;
import ru.practicum.moderate.entity.ReviewSort;
import ru.practicum.moderate.entity.ReviewState;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static ru.practicum.constants.Constants.EVENT_NOT_EXISTS;
import static ru.practicum.constants.Constants.REVIEW_NOT_EXISTS;
import static ru.practicum.constants.Constants.checkPageable;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private static final String CREATED = "created";
    private static final String STATE = "state";
    private static final String REASON = "reason";
    private static final String EVENT_ID = "event_id";
    private final ReviewRepository reviewRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final ReviewMapper reviewMapper;
    private final Sort orderByCreatedDesc = Sort.by(CREATED).descending();

    @Override
    public EventDto save(long eventId, NewReviewDto dto) {
        Event event = getEvent(eventId);
        dto.setEventId(eventId);
        Review review = reviewMapper.toEntity(dto);
        review.setEvent(event);
        review.setCreated(LocalDateTime.now());
        reviewRepository.save(review);

        event.setReviews(getReviewsForEvent(eventId));

        return eventMapper.toDto(event);
    }

    @Override
    public ReviewDto get(long eventId, long reviewId) {
        isExistsEvent(eventId);

        return reviewMapper.toDto(
                reviewRepository.findByIdAndEvent_Id(reviewId, eventId)
                        .orElseThrow(() ->
                                new NotFoundException(format(REVIEW_NOT_EXISTS, reviewId))
                        )
        );
    }

    @Override
    public List<ReviewDto> getAllByEvent(long eventId) {
        return getReviewsForEvent(eventId).stream()
                .map(reviewMapper::toDto)
                .collect(toList());
    }

    @Override
    public List<ReviewDto> getAll(String text,
                                  List<String> states,
                                  LocalDateTime rangeStart,
                                  LocalDateTime rangeFinish,
                                  String sort,
                                  Integer from,
                                  Integer size) {
        Pageable pageable = checkPageable(from, size, getSort(sort));
        List<Specification<Review>> specifications = searchInRangePeriod(rangeStart, rangeFinish);
        if (text != null) {
            specifications.add(searchText(text));
        }
        if (states != null) {
            specifications.add(((root, query, criteriaBuilder) ->
                    criteriaBuilder.in(root.get(STATE)).value(getReviewStates(states))));
        }
        return getReviewsBySearchSpecification(specifications, pageable).stream()
                .map(reviewMapper::toDto)
                .collect(toList());
    }

    private List<ReviewState> getReviewStates(List<String> reviewStates) {
        List<ReviewState> list = new ArrayList<>();
        for (String state : reviewStates) {
            state = state.toUpperCase();
            if (ReviewState.isValid(state)) {
                list.add(ReviewState.valueOf(state));
            }
        }

        return list;
    }

    private Sort getSort(String sort) {
        if (sort == null) return null;
        ReviewSort anEnum = EnumUtils.getEnum(ReviewSort.class, sort.toUpperCase());
        switch (anEnum) {
            case EVENT:
                return Sort.by(EVENT_ID).descending();
            case STATE:
                return Sort.by(STATE).ascending();
            case REASON:
                return Sort.by(REASON).ascending();
            case CREATED:
                return orderByCreatedDesc;
            default:
                throw new BadRequestException("Wrong sorting parameters");
        }
    }

    private List<Review> getReviewsBySearchSpecification(List<Specification<Review>> specifications, Pageable pageable) {
        Specification<Review> reviewSpecification = specifications.stream()
                .filter(Objects::nonNull)
                .reduce(Specification::and)
                .orElseThrow(NullPointerException::new);
        return reviewRepository.findAll(reviewSpecification, pageable).toList();
    }

    private List<Specification<Review>> searchInRangePeriod(LocalDateTime rangeStart, LocalDateTime rangeFinish) {
        List<Specification<Review>> specifications = new ArrayList<>();
        LocalDateTime finalRangeStart = rangeStart == null ? LocalDateTime.now() : rangeStart;
        specifications.add(((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get(CREATED),
                        finalRangeStart)));
        if (rangeFinish != null) {
            specifications.add(((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThan(root.get(CREATED),
                            rangeFinish)));
        }

        return specifications;
    }

    private Specification<Review> searchText(@NotNull String text) {
        String finalText = text.trim().toUpperCase();
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.like(
                                criteriaBuilder.upper(
                                        root.get(REASON)),
                                "%" + finalText + "%"),
                        criteriaBuilder.like(
                                criteriaBuilder.upper(
                                        root.get("comment")),
                                "%" + finalText + "%")));
    }


    private Event getEvent(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new NotFoundException(format(EVENT_NOT_EXISTS, eventId)));
    }

    private List<Review> getReviewsForEvent(long eventId) {
        return reviewRepository.findByEvent_Id(eventId, orderByCreatedDesc);
    }

    private void isExistsEvent(long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException(format(EVENT_NOT_EXISTS, eventId));
        }
    }
}
