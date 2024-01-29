package ru.practicum.event.request.api.service;

import ru.practicum.event.request.api.dto.EventRequestDto;

import java.util.List;

public interface EventRequestService {
    List<EventRequestDto> getAllRequests(Long userId, Integer from, Integer size);

    EventRequestDto createRequest(long userId, Long eventId);

    EventRequestDto cancelRequest(long userId, long requestId);
}
