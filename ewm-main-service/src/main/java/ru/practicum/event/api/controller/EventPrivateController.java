package ru.practicum.event.api.controller;

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
import ru.practicum.event.api.dto.NewEventDto;
import ru.practicum.event.api.dto.UpdateEventDto;
import ru.practicum.event.api.service.EventServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

import static prototype.Constants.FROM;
import static prototype.Constants.SIZE;

@RestController
@Slf4j
@Validated
public class EventPrivateController extends EventController {

    public EventPrivateController(EventServiceImpl service) {
        super(service);
    }

    @GetMapping({"/users/{userId}/events"})
    public List<EventDto> getByInitializer(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = FROM) @Min(0) Integer from,
            @RequestParam(required = false, defaultValue = SIZE) @Min(1) Integer size) {
        log.debug("[i] get events by initializer ID:{}", userId);

        return service.getByInitializer(userId, from, size);
    }

    @PostMapping({"/users/{userId}/events"})
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto create(@PathVariable Long userId,
                           @RequestBody @Valid NewEventDto newEventDto) {
        log.debug("[i] new event");

        return service.create(userId, newEventDto);
    }

    @GetMapping({"/users/{userId}/events/{eventId}"})
    public EventDto getByInitializerAndId(@PathVariable Long userId,
                                          @PathVariable Long eventId) {
        log.debug("[i] get event ID:{} by initializer ID:{}", eventId, userId);

        return service.getByInitializerAndId(userId, eventId);
    }

    @PatchMapping({"/users/{userId}/events/{eventId}"})
    public EventDto updateByInitializerAndId(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Valid UpdateEventDto updateEventDto) {
        log.debug("[i] get event ID:{} by initializer ID:{}", eventId, userId);

        return service.updateByInitializerAndId(userId, eventId, updateEventDto);
    }

    @GetMapping({"/users/{userId}/events/{eventId}/requests"})
    public List<EventDto> getRequestsInEvent(@PathVariable Long userId,
                                             @PathVariable Long eventId) {
        log.debug("[i] get request in event ID:{} from user ID:{}", eventId, userId);

        return service.getRequestsInEvent(userId, eventId);
    }

    @PatchMapping({"/users/{userId}/events/{eventId}/requests"})
    public List<EventDto> updateRequestsInEvent(@PathVariable Long userId,
                                                @PathVariable Long eventId) {
        log.debug("[i] update request in event ID:{} from user ID:{}", eventId, userId);

        return service.updateRequestsInEvent(userId, eventId);
    }
}
