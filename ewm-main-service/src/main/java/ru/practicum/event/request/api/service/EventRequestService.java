package ru.practicum.event.request.api.service;

import ru.practicum.event.request.api.dto.EventRequestDto;

import java.util.List;

public interface EventRequestService {
    List<EventRequestDto> getAllRequests(Long userId, Integer from, Integer size);

    EventRequestDto createRequest(EventRequestDto dto);

    EventRequestDto cancelRequest(EventRequestDto dto);

    /**
     * Состояние запроса пользователя на участие в событии для участника
     */
    enum RequestState {
        PENDING,
        CONFIRMED,
        REJECTED,
        CANCELED
    }
}
