package ru.practicum.moderate.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.api.dto.EventDto;
import ru.practicum.moderate.api.dto.NewReviewDto;
import ru.practicum.moderate.api.dto.ReviewDto;
import ru.practicum.moderate.api.service.ReviewService;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.constants.Constants.DATE_FORMAT;
import static ru.practicum.constants.Constants.FROM;
import static ru.practicum.constants.Constants.SIZE;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService service;

    @PatchMapping("/events/{eventId}/review")
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto save(@PathVariable Long eventId,
                         @RequestBody NewReviewDto dto) {
        log.debug("[i] begin review for event with ID:{}", eventId);

        return service.save(eventId, dto);
    }

    @GetMapping("/events/{eventId}/review/{reviewId}")
    public ReviewDto get(@PathVariable Long eventId,
                         @PathVariable Long reviewId) {
        log.debug("[i] get review ID:{} for event ID:{}", reviewId, eventId);

        return service.get(eventId, reviewId);
    }

    @GetMapping("/events/{eventId}/review")
    public List<ReviewDto> getAllByEvent(@PathVariable Long eventId) {
        log.debug("[i] get all reviews for event (ID:{})", eventId);

        return service.getAllByEvent(eventId);
    }

    @GetMapping("/admin/review")
    public List<ReviewDto> getAll(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false)
            @DateTimeFormat(fallbackPatterns = DATE_FORMAT) LocalDateTime rangeStart,
            @RequestParam(required = false)
            @DateTimeFormat(fallbackPatterns = DATE_FORMAT) LocalDateTime rangeEnd,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = FROM) @Min(0) Integer from,
            @RequestParam(required = false, defaultValue = SIZE) @Min(1) Integer size) {
        log.debug("[i] get All reviews by filter");

        return service.getAll(text, states, rangeStart, rangeEnd, sort, from, size);
    }
}
