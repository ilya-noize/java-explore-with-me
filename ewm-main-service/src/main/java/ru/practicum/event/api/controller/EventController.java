package ru.practicum.event.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.api.dto.EventDto;
import ru.practicum.event.api.dto.EventShortDto;
import ru.practicum.event.api.dto.NewEventDto;
import ru.practicum.event.api.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

import static prototype.Constants.FROM;
import static prototype.Constants.SIZE;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventController {
    private final EventService service;

    @PostMapping({"/users/{userId}/events"})
    public EventDto create(@PathVariable Long userId,
                           @RequestBody @Valid NewEventDto newEventDto) {
        log.debug("[i] new event");
        return service.create(userId, newEventDto);
    }

    @PatchMapping({"/admin/events/{id}"})
    public EventDto update(@PathVariable Long id, @RequestBody @Valid EventShortDto newDto) {
        log.debug("[i][admin] update event ID:{}", id);

        return service.update(id, newDto);
    }

    @GetMapping({"/events/{id}"})
    public EventDto get(@PathVariable Long id) {
        log.debug("[i] get event ID:{}", id);
        return service.getById(id);
    }

    @GetMapping({"/users/{userId}/events"})
    public List<EventDto> getByInitializer(@PathVariable Long userId) {
        log.debug("[i] get events by initializer ID:{}", userId);
        return service.getByInitializer(userId);
    }

    @GetMapping({"/users/{userId}/events/{id}"})
    public List<EventDto> getByInitializerAndId(@PathVariable Long userId, @PathVariable Long id) {
        log.debug("[i] get event ID:{} by initializer ID:{}", id, userId);
        return service.getByInitializerAndId(userId, id);
    }

    @GetMapping({"/events"})
    public List<EventDto> getAll(@RequestParam(required = false, defaultValue = FROM) @Min(0) Integer from,
                                 @RequestParam(required = false, defaultValue = SIZE) @Min(1) Integer size) {
        log.debug("[i] get all events");
        return service.getAll(from, size);
    }
}
