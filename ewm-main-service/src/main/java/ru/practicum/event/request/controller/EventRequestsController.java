package ru.practicum.event.request.controller;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import prototype.Controller;
import ru.practicum.event.request.dto.NewEventRequestDto;
import ru.practicum.event.request.dto.ParticipationRequestDto;

@RestController
@Validated
public class EventRequestsController implements Controller<ParticipationRequestDto, NewEventRequestDto> {

    public ParticipationRequestDto create(@RequestBody @Valid NewEventRequestDto newDto) {
        return this.update(newDto.getEventId(), newDto);
    }

    public ParticipationRequestDto update(Long id, NewEventRequestDto newDto) {
        return this.update(newDto.getOrganizerId(), newDto.getEventId(), newDto);
    }

    @PatchMapping({"/users/{userId}/events/{id}/requests"})
    public ParticipationRequestDto update(@PathVariable Long userId,
                                          @PathVariable Long id,
                                          @RequestBody @Valid NewEventRequestDto dto) {
        dto.setOrganizerId(userId);
        dto.setEventId(id);
        return null;
    }

    public ParticipationRequestDto get(@PathVariable Long id) {
        return null;
    }

    @GetMapping({"/admin/events"})
    public List<ParticipationRequestDto> getAll() {
        return List.of(new ParticipationRequestDto());
    }

    @GetMapping({"/users/{userId}/events/{id}/requests"})
    public List<ParticipationRequestDto> getAll(@PathVariable Long userId,
                                                @PathVariable Long id,
                                                @RequestParam(defaultValue = "0") @Min(0L) Integer from,
                                                @RequestParam(defaultValue = "10") @Min(1L) Integer size) {
        return null;
    }

    public void remove(@PathVariable Long id) {
    }
}
