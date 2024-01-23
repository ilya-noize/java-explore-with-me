package ru.practicum.event.api.controller;

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
import ru.practicum.event.api.dto.EventDto;
import ru.practicum.event.api.dto.EventShortDto;
import ru.practicum.event.api.dto.NewEventDto;
import ru.practicum.event.api.dto.UpdateEventDto;
import ru.practicum.event.api.service.EventService;
import ru.practicum.event.request.api.dto.EventRequestDto;
import ru.practicum.event.request.api.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.request.api.dto.EventRequestStatusUpdateResult;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.constants.Constants.FROM;
import static ru.practicum.constants.Constants.SIZE;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class EventPrivateController {

    private final EventService service;

    @GetMapping({"/users/{userId}/events"})
    public List<EventShortDto> getByInitializer(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = FROM) @Min(0) Integer from,
            @RequestParam(required = false, defaultValue = SIZE) @Min(1) Integer size) {
        log.debug("[i] Получение событий, добавленных текущим пользователем ID:{}", userId);

        return service.getByInitializer(userId, from, size);
    }

    @PostMapping({"/users/{userId}/events"})
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto create(@PathVariable Long userId,
                           @RequestBody @Valid NewEventDto dto) {
        log.info("[i] Добавление нового события \ndto:{}", dto);

        return service.create(userId, dto);
    }

    @GetMapping({"/users/{userId}/events/{eventId}"})
    public EventDto getByInitializerAndId(@PathVariable Long userId,
                                          @PathVariable Long eventId) {
        log.debug("[i] Получение полной информации о добавленном событии ID:{} " +
                "текущим пользователем ID:{}", eventId, userId);

        return service.getByInitializerAndId(userId, eventId);
    }

    @PatchMapping({"/users/{userId}/events/{eventId}"})
    public EventDto updateByInitializerAndId(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Valid UpdateEventDto dto) {
        log.debug("[i] Изменение добавленного события ID:{} текущим пользователем ID:{}", eventId, userId);

        return service.updateByInitializerAndId(userId, eventId, dto);
    }

    @GetMapping({"/users/{userId}/events/{eventId}/requests"})
    public List<EventRequestDto> getRequestsInEvent(@PathVariable Long userId,
                                                    @PathVariable Long eventId) {
        log.debug("[i] Получение информации о запросах на участие в событии ID:{} " +
                "текущего пользователя ID:{}", eventId, userId);

        return service.getRequestsInEvent(userId, eventId);
    }

    @PatchMapping({"/users/{userId}/events/{eventId}/requests"})
    public EventRequestStatusUpdateResult updateRequestsInEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.debug("[i] Изменение статуса (подтверждена, отменена) " +
                "заявок на участие в событии ID:{} текущего пользователя ID:{}", eventId, userId);

        return service.updateRequestsInEvent(userId, eventId, eventRequestStatusUpdateRequest);
    }
}
