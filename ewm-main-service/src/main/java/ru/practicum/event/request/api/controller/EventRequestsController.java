package ru.practicum.event.request.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
        log.debug("[i] Получение информации о заявках текущего пользователя ID:{} " +
                "на участие в чужих событиях", userId);
        return service.getAllRequests(userId, from, size);
    }

    @PostMapping({"/users/{userId}/requests"})
    @ResponseStatus(HttpStatus.CREATED)
    public EventRequestDto createRequest(@PathVariable Long userId,
                                         @RequestParam Long eventId) {
        log.debug("[i] Добавление запроса от текущего пользователя ID:{} " +
                "на участие в событии ID:{}", userId, eventId);
        return service.createRequest(userId, eventId);
    }

    @PatchMapping({"/users/{userId}/requests/{requestId}/cancel"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public EventRequestDto cancelRequest(@PathVariable Long userId,
                              @PathVariable Long requestId,
                              @RequestBody EventRequestDto dto) {
        dto.setId(requestId);
        dto.setRequester(userId);

        return service.cancelRequest(dto);
    }
}
