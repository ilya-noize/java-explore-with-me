package ru.practicum.event.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.event.api.dto.EventDto;
import ru.practicum.event.api.dto.EventShortDto;
import ru.practicum.event.api.dto.NewEventDto;
import ru.practicum.event.api.mapper.EventMapper;
import ru.practicum.event.api.repository.EventRepository;
import ru.practicum.event.entity.Event;
import ru.practicum.exception.BadRequestException;
import ru.practicum.user.api.repository.UserRepository;
import ru.practicum.user.entity.User;

import java.util.List;

import static prototype.Constants.USER_NOT_EXISTS;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventDto create(long userId, NewEventDto newEventDto) {
        User initiator = isExistUserById(userId);
        Event event = EventMapper.INSTANCE.toEntity(newEventDto, initiator);
        return EventMapper.INSTANCE.toDto(
                eventRepository.save(event));
    }

    private User isExistUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(
                        String.format(USER_NOT_EXISTS, id)));
    }

    public EventDto update(Long id, EventShortDto eventShortDto) {
        return null;
    }

    public EventDto getById(Long id) {
        return null;
    }

    public List<EventDto> getByInitializer(Long userId) {
        return null;
    }

    public List<EventDto> getByInitializerAndId(Long userId, Long id) {
        return null;
    }

    public List<EventDto> getAll(Integer from, Integer size) {
        return null;
    }
}
