package ru.practicum.event.request.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.request.api.dto.EventRequestDto;
import ru.practicum.event.request.api.service.EventRequestService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class EventRequestsController {
    private final EventRequestService service;

    @GetMapping({"/users/{userId}/requests"})
    public List<EventRequestDto> getAllRequests(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") @Min(0L) Integer from,
            @RequestParam(defaultValue = "10") @Min(1L) Integer size) {
        log.debug("[i] Getting information about requests of the current user ID:{} " +
                "to participate in other people's events", userId);
        List<EventRequestDto> response = service.getAllRequests(userId, from, size);
        log.debug("[✓] Information about the current user's requests has been received:");

        return response;
    }

    @PostMapping({"/users/{userId}/requests"})
    @ResponseStatus(HttpStatus.CREATED)
    public EventRequestDto createRequest(@PathVariable Long userId,
                                         @RequestParam Long eventId) {
        log.debug("[i] Adding a request from the current user ID:{} \n" +
                " to participate in the event ID:{}", userId, eventId);
        EventRequestDto response = service.createRequest(userId, eventId);
        log.debug("[✓] The request has been added.");

        return response;
    }

    @PatchMapping({"/users/{userId}/requests/{requestId}/cancel"})
    @ResponseStatus(HttpStatus.OK)
    public EventRequestDto cancelRequest(@PathVariable Long userId,
                                         @PathVariable Long requestId) {
        log.debug("[i] Canceling your request to participate in the event");
        EventRequestDto response = service.cancelRequest(userId, requestId);
        log.debug("[✓] The request been cancelled");

        return response;
    }
}
