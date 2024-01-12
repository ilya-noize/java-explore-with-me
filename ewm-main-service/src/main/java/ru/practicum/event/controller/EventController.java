package ru.practicum.event.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import prototype.Controller;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;

@Validated
public class EventController implements Controller<EventDto, EventShortDto> {
    public EventController() {
    }

    @PostMapping({"/users/{userId}/events"})
    public EventDto create(@PathVariable Long userId, @RequestBody @Valid NewEventDto newEventDto) {
        return null;
    }

    public EventDto create(EventShortDto newDto) {
        return null;
    }

    @PatchMapping({"/admin/events/{id}"})
    public EventDto update(@PathVariable Long id, @RequestBody @Valid EventShortDto newDto) {
        return null;
    }

    @GetMapping({"/events/{id}"})
    public EventDto get(@PathVariable Long id) {
        return null;
    }

    @GetMapping({"/users/{userId}/events"})
    public EventDto getByOrganizer(@PathVariable Long userId) {
        return null;
    }

    @GetMapping({"/users/{userId}/events/{id}"})
    public EventDto getByOrganizerAndId(@PathVariable Long userId, @PathVariable Long id) {
        return null;
    }

    @GetMapping({"/events"})
    public List<EventDto> getAll() {
        return List.of(new EventDto());
    }

    public void remove(Long id) {
    }
}
