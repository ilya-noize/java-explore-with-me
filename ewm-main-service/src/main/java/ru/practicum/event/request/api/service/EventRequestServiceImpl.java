package ru.practicum.event.request.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.constants.Constants;
import ru.practicum.event.api.repository.EventRepository;
import ru.practicum.event.entity.Event;
import ru.practicum.event.request.api.dto.EventRequestDto;
import ru.practicum.event.request.api.mapper.EventRequestMapper;
import ru.practicum.event.request.api.repository.EventRequestRepository;
import ru.practicum.event.request.entity.EventRequest;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.api.repository.UserRepository;
import ru.practicum.user.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static ru.practicum.constants.Constants.EVENT_NOT_EXISTS;
import static ru.practicum.constants.Constants.EVENT_REQUEST_NOT_EXISTS;
import static ru.practicum.constants.Constants.RequestState.CANCELED;
import static ru.practicum.constants.Constants.RequestState.CONFIRMED;
import static ru.practicum.constants.Constants.RequestState.PENDING;
import static ru.practicum.constants.Constants.USER_NOT_EXISTS;
import static ru.practicum.constants.Constants.checkPageable;

@RestController
@Slf4j
@RequiredArgsConstructor
public class EventRequestServiceImpl implements EventRequestService {
    private final EventRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<EventRequestDto> getAllRequests(Long userId, Integer from, Integer size) {
        validateUser(userId);
        List<EventRequest> requests = requestRepository
                .findByRequester_Id(userId, checkPageable(from, size, null))
                .orElse(new ArrayList<>());

        return requests.stream()
                .map(EventRequestMapper.INSTANCE::toDto)
                .collect(toList());
    }

    @Override
    public EventRequestDto createRequest(long userId, Long eventId) {
        if (eventId == null) {
            throw new BadRequestException("A required parameter `eventId` is omitted.");
        }
        User requester = validateUser(userId);
        Event event = validateEvent(eventId);
        if (userId == event.getInitiator().getId()) {
            throw new ConflictException("The initiator of the event cannot submit a request " +
                    "to participate in his event");
        }
        if (requestRepository.existsByRequester_IdAndEvent_Id(userId, eventId)) {
            throw new ConflictException(format("The user's (ID:%d) request " +
                    "to participate in the user's event (ID:%d) exists", userId, eventId));
        }
        if (!event.getState().equals(Constants.EventState.PUBLISHED)) {
            throw new ConflictException("Cannot participate in an unpublished event");
        }
        EventRequest request = EventRequest.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(requester)
                .status(getEventRequestState(event))
                .build();

        return EventRequestMapper.INSTANCE.toDto(
                requestRepository.save(request));
    }

    @Override
    public EventRequestDto cancelRequest(long userId, long requestId) {
        User requester = validateUser(userId);
        EventRequest request = validateEventRequest(requestId);
        if (!request.getRequester().equals(requester)) {
            throw new NotFoundException(format(EVENT_REQUEST_NOT_EXISTS, requestId));
        }
        request.setStatus(CANCELED);

        return EventRequestMapper.INSTANCE.toDto(requestRepository.save(request));
    }

    /**
     * Для автоматической установки статуса нового запроса на участии в событии
     * <p>
     * Если лимит участников не ограничен (равен 0), принять запрос;<br/>
     * Если модерация запросов включена, отправить на подтверждение инициатору события;<br/>
     * Если модерация запросов выключена, принять запрос;<br/>
     *
     * @param event Event
     * @return Request State
     */
    private Constants.RequestState getEventRequestState(Event event) {
        long confirmedRequests = event.getConfirmedRequests();
        int participantLimit = event.getParticipantLimit();
        if (participantLimit == 0) {
            return CONFIRMED;
        }
        if (event.isRequestModeration()) {
            return PENDING;
        } else {
            if (confirmedRequests + 1 > participantLimit) {
                throw new ConflictException("It is not possible to confirm the request " +
                        "if the limit on requests for this event has already been reached");
            }
        }
        return CONFIRMED;
    }

    private User validateUser(long userId) {

        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(format(USER_NOT_EXISTS, userId)));
    }

    private Event validateEvent(long eventId) {

        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(format(EVENT_NOT_EXISTS, eventId)));
    }

    private EventRequest validateEventRequest(long requestId) {

        return requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException(format(EVENT_REQUEST_NOT_EXISTS, requestId)));
    }
}
