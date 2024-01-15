package ru.practicum.event.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.event.api.dto.EventDto;
import ru.practicum.event.api.dto.EventShortDto;
import ru.practicum.event.api.dto.NewEventDto;
import ru.practicum.event.api.dto.UpdateEventAdminDto;
import ru.practicum.event.api.dto.UpdateEventDto;
import ru.practicum.event.api.mapper.EventMapper;
import ru.practicum.event.api.repository.EventRepository;
import ru.practicum.event.entity.Event;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.api.repository.UserRepository;
import ru.practicum.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;
import static prototype.Constants.EVENT_NOT_EXISTS;
import static prototype.Constants.USER_NOT_EXISTS;
import static prototype.Constants.checkPageable;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    private User isExistUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new BadRequestException(
                                format(USER_NOT_EXISTS, id)
                        )
                );
    }

    @Override
    public EventDto create(long userId, NewEventDto newEventDto) {
        // 400
        // 409 - Событие не удовлетворяет правилам создания
        User initiator = isExistUserById(userId);
        Event event = EventMapper.INSTANCE.toEntity(newEventDto, initiator);
        return EventMapper.INSTANCE.toDto(
                eventRepository.save(event));
    }

    @Override
    public List<EventDto> getByInitializer(Long userId, Integer from, Integer size) {
        return null;
    }

    @Override
    public EventDto getByInitializerAndId(Long userId, Long eventId) {
        User initiator = isExistUserById(userId);
        Event event = eventRepository.getByInitiatorAndId(initiator, eventId)
                .orElseThrow(() ->
                        new NotFoundException(format(EVENT_NOT_EXISTS, eventId)));

        return EventMapper.INSTANCE.toDto(event);
    }

    @Override
    public EventDto updateByInitializerAndId(Long userId, Long eventId, UpdateEventDto updateEventDto) {
        //400 - request error
        //404 - not fount event
        //409 - Событие не удовлетворяет правилам редактирования: only pending or cancelled
        return null;
    }

    @Override
    public List<EventDto> getRequestsInEvent(Long userId, Long eventId) {
        //400 - request error
        return null;
    }

    @Override
    public List<EventDto> updateRequestsInEvent(Long userId, Long eventId) {
        //400 - request error
        //404 - not fount event
        //409 - Достигнут лимит одобренных заявок
        return null;
    }

    @Override
    public EventDto eventAdministration(Long eventId, UpdateEventAdminDto updateEventAdminDto) {
        return null;
    }

    @Override
    public List<EventDto> getEventsAdministration(
            List<Long> users,
            List<String> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Integer from,
            Integer size) {
        Pageable pageable = checkPageable(from, size);

        return null;
    }

    @Override
    public List<EventShortDto> getAll(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            String sort,
            Integer from,
            Integer size) {
        Pageable pageable = checkPageable(from, size);

        return null;
    }

    @Override
    public EventDto get(Long eventId) {
        return null;
    }
}
