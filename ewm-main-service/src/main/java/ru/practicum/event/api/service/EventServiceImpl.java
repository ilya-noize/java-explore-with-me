package ru.practicum.event.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.HitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.category.api.repository.CategoryRepository;
import ru.practicum.category.entity.Category;
import ru.practicum.constants.Constants.StateAdminAction;
import ru.practicum.event.api.dto.EventDto;
import ru.practicum.event.api.dto.EventShortDto;
import ru.practicum.event.api.dto.NewEventDto;
import ru.practicum.event.api.dto.UpdateEventAdminDto;
import ru.practicum.event.api.dto.UpdateEventDto;
import ru.practicum.event.api.mapper.EventMapper;
import ru.practicum.event.api.repository.EventRepository;
import ru.practicum.event.api.repository.specs.EventSpecification;
import ru.practicum.event.api.repository.specs.SearchCriteria;
import ru.practicum.event.api.repository.specs.SearchOperation;
import ru.practicum.event.entity.Event;
import ru.practicum.event.request.api.dto.EventRequestDto;
import ru.practicum.event.request.api.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.request.api.dto.EventRequestStatusUpdateResult;
import ru.practicum.event.request.api.mapper.EventRequestMapper;
import ru.practicum.event.request.api.repository.EventRequestRepository;
import ru.practicum.event.request.api.service.EventRequestService;
import ru.practicum.event.request.entity.EventRequest;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.location.entity.Location;
import ru.practicum.location.repository.LocationRepository;
import ru.practicum.service.StatisticService;
import ru.practicum.user.api.repository.UserRepository;
import ru.practicum.user.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static ru.practicum.constants.Constants.CATEGORY_NOT_EXISTS;
import static ru.practicum.constants.Constants.EVENT_NOT_EXISTS;
import static ru.practicum.constants.Constants.USER_NOT_EXISTS;
import static ru.practicum.constants.Constants.checkPageable;
import static ru.practicum.event.request.api.service.EventRequestService.RequestState.CONFIRMED;
import static ru.practicum.event.request.api.service.EventRequestService.RequestState.PENDING;
import static ru.practicum.event.request.api.service.EventRequestService.RequestState.REJECTED;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private EventRepository eventRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private EventRequestRepository requestRepository;
    private LocationRepository locationRepository;
    private final StatisticService statisticService;

    @Override
    public EventDto create(long userId, NewEventDto dto) {
        // 400
        // 409 - Событие не удовлетворяет правилам создания
        User initiator = getUser(userId);
        Category category = getCategory(dto.getCategoryId());
        LocalDateTime eventDate = dto.getEventDate();
        validateEventDate(eventDate);

        Event event = EventMapper.INSTANCE.toEntity(dto);
        event.setEventDate(eventDate);
        event.setCategory(category);
        event.setInitiator(initiator);
        event.setState(EventState.PENDING);

        return EventMapper.INSTANCE.toDto(
                eventRepository.save(event), event.getConfirmedRequests().size());
    }

    @Override
    public List<EventShortDto> getByInitializer(Long userId, Integer from, Integer size) {
        Pageable pageable = checkPageable(from, size, null);
        User initiator = getUser(userId);
        List<Event> events = eventRepository.getByInitiator(initiator, pageable)
                .orElse(emptyList());

        return events.stream()
                .map(EventMapper.INSTANCE::toShortDto)
                .collect(toList());
    }

    @Override
    public EventDto getByInitializerAndId(Long userId, Long eventId) {
        User initiator = getUser(userId);
        Event event = eventRepository.getByInitiatorAndId(initiator, eventId)
                .orElseThrow(() ->
                        new NotFoundException(format(EVENT_NOT_EXISTS, eventId)));

        return EventMapper.INSTANCE.toDto(event, event.getConfirmedRequests().size());
    }

    @Override
    public EventDto updateByInitializerAndId(Long userId, Long eventId, UpdateEventDto dto) {
        User initiator = getUser(userId);
        Event event = eventRepository.getByInitiatorAndId(initiator, eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_EXISTS));
        EventState eventState = event.getState();
        if (eventState.equals(EventState.PUBLISHED)) {
            throw new ForbiddenException(format("Wrong event state: %s", eventState));
        }

        LocalDateTime newEventDate = dto.getEventDate();
        if (newEventDate != null) {
            validateEventDate(newEventDate);
            event.setEventDate(newEventDate);
        }

        updateTitleAnnotationDescriptionCategoryLocationPaidParticipantLimitModeration(event,
                dto.getTitle(),
                dto.getAnnotation(),
                dto.getDescription(),
                dto.getCategory(),
                getLocation(dto.getLocation()),
                dto.getPaid(),
                dto.getParticipantLimit(),
                dto.getRequestModeration()
        );

        StateAction stateAction = dto.getStateAction();
        if (stateAction != null) {
            switch (stateAction) {
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
            }
        }

        return EventMapper.INSTANCE.toDto(
                eventRepository.save(event),
                event.getConfirmedRequests().size());
    }

    @Override
    public List<EventRequestDto> getRequestsInEvent(Long userId, Long eventId) {
        //400 - request error
        isExistsUserLikeInitiatorEvent(userId, eventId);
        return requestRepository.getByEvent_Id(eventId).stream()
                .map(EventRequestMapper.INSTANCE::toDto)
                .collect(toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestsInEvent(
            Long userId,
            Long eventId,
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        isExistsUser(userId);
        Event event = getEvent(eventId);
        validateConfirmation(event);
        List<Long> requestIds = eventRequestStatusUpdateRequest.getRequestIds();
        List<EventRequest> requests = requestRepository.findAllById(requestIds);
        int limit = requestRepository.countByEvent_IdAndStatus(eventId, CONFIRMED);
        int participantLimit = event.getParticipantLimit();
        validateLimitRequests(participantLimit, limit);

        EventRequestStatusUpdateResult statusUpdateResponse = new EventRequestStatusUpdateResult();
        EventRequestService.RequestState updateStatus = eventRequestStatusUpdateRequest.getStatus();
        for (EventRequest request : requests) {
            validateRequestStatus(request.getStatus());
            if (updateStatus.equals(CONFIRMED)) {
                if (limit < participantLimit) {
                    request.setStatus(CONFIRMED);
                    limit++;
                } else {
                    request.setStatus(REJECTED);
                }
            } else {
                request.setStatus(REJECTED);
            }
            statusUpdateResponse.getConfirmedRequests().add(EventRequestMapper.INSTANCE.toDto(request));
        }
        requestRepository.saveAll(requests);
        return statusUpdateResponse;
    }

    @Override
    public EventDto eventAdministration(Long eventId, UpdateEventAdminDto dto) {
        Event event = getEvent(eventId);
        LocalDateTime eventDate = event.getEventDate();
        validateAdminTimeMoment(eventDate);

        LocalDateTime newEventDate = dto.getEventDate();
        validateAdminTimeMoment(newEventDate);

        String stateAction = dto.getStateAction();
        EventState eventState = event.getState();
        EventState newEventState = getValidAdminEventState(
                StateAdminAction.getInstance(stateAction), eventState);
        event.setState(stateAction != null ? newEventState : event.getState());
        if (event.getState().equals(EventState.PUBLISHED)) {
            event.setPublishedOn(LocalDateTime.now());
        }
        updateTitleAnnotationDescriptionCategoryLocationPaidParticipantLimitModeration(event,
                dto.getTitle(),
                dto.getAnnotation(),
                dto.getDescription(),
                dto.getCategory(),
                getLocation(dto.getLocation()),
                dto.getPaid(),
                dto.getParticipantLimit(),
                dto.getRequestModeration());

        return EventMapper.INSTANCE.toDto(
                eventRepository.save(event),
                event.getConfirmedRequests().size());
    }

    @Override
    public List<EventDto> getEventsAdministration(
            List<Long> users,
            List<String> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeFinish,
            Integer from,
            Integer size) {
        validateRange(rangeStart, rangeFinish);
        Pageable pageable = checkPageable(from, size, null);
        EventSpecification eventSpecification = new EventSpecification();

        eventSpecification.add(getCriteriaListUsers(users));
        eventSpecification.add(getCriteriaListCategories(categories));
        eventSpecification.add(getCriteriaStates(states));
        eventSpecification.add(getCriteriaRangeStart(rangeStart));
        eventSpecification.add(getCriteriaRangeFinish(rangeFinish));

        List<Event> events = getEventsBySpecs(eventSpecification, pageable);

        return events.stream()
                .map(event -> EventMapper.INSTANCE.toDto(event,
                        event.getConfirmedRequests().size()))
                .collect(toList());
    }

    @Override
    public List<EventShortDto> getAll(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeFinish,
            Boolean onlyAvailable,
            String sort,
            HttpServletRequest httpServletRequest,
            Integer from,
            Integer size) {
        Pageable pageable = checkPageable(from, size, getSort(sort));
        if (rangeStart == null) rangeStart = LocalDateTime.now();
        validateRange(rangeStart, rangeFinish);
        EventSpecification eventSpecification = new EventSpecification();
        if (text != null) {
            text = text.toUpperCase();
            eventSpecification.add(getCriteriaTextInTitle(text));
            eventSpecification.add(getCriteriaTextInAnnotation(text));
            eventSpecification.add(getCriteriaTextInDescription(text));
        }
        eventSpecification.add(getCriteriaListCategories(categories));
        eventSpecification.add(getCriteriaPaid(paid));
        eventSpecification.add(getCriteriaRangeStart(rangeStart));
        eventSpecification.add(getCriteriaRangeFinish(rangeFinish));
        if (onlyAvailable) eventSpecification.add(getCriteriaParticipantLimitNotDefined());
        List<Event> events = getEventsBySpecs(eventSpecification, pageable);

        return events.stream()
                .filter(event -> {
                    if (onlyAvailable && event.getParticipantLimit() != 0) {
                        addHit(httpServletRequest);
                        return event.getParticipantLimit() > event.getConfirmedRequests().size();
                    }
                    addHit(httpServletRequest);
                    return true;
                })
                .map(EventMapper.INSTANCE::toShortDto)
                .collect(toList());
    }


    @Override
    public EventDto get(Long eventId, HttpServletRequest httpRequest) {
        Event event = getEvent(eventId);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException(format(EVENT_NOT_EXISTS, eventId));
        }
        addHit(httpRequest);
//        event.setViews(event.getViews() + 1);
        event.setViews(getViews(eventId));
        return EventMapper.INSTANCE.toDto(event,
                event.getConfirmedRequests().size());
    }

    private List<Event> getEventsBySpecs(EventSpecification eventSpecification, Pageable pageable) {
        if (eventSpecification.getList().isEmpty()) {

            return eventRepository.findAll(eventSpecification, pageable).toList();
        } else {

            return eventRepository.findAll(pageable);
        }
    }

    private void addHit(HttpServletRequest httpServletRequest) {
        statisticService.post(HitDto.builder()
                .app("ewm-main-service")
                .uri(httpServletRequest.getRequestURI())
                .ip(httpServletRequest.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());
    }

    private Integer getViews(long eventId) {
        ResponseEntity<ViewStatsDto[]> response = statisticService.getData(
                LocalDateTime.now().minusYears(1),
                LocalDateTime.now(),
                new String[]{"/events/" + eventId},
                true);
        int views = 0;
        Optional<ViewStatsDto> stat;
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            stat = Arrays.stream(response.getBody()).findFirst();
            if (stat.isPresent()) {
                views = Math.toIntExact(stat.get().getHits());
            }
        }
        return views;
    }

    private void updateTitleAnnotationDescriptionCategoryLocationPaidParticipantLimitModeration(
            Event event,
            String title,
            String annotation,
            String description,
            Long categoryId,
            Location location,
            Boolean paid,
            Integer participantLimit,
            Boolean requestModeration) {
        event.setTitle(title != null ? title : event.getTitle());
        event.setAnnotation(annotation != null ? annotation : event.getAnnotation());
        event.setDescription(description != null ? description : event.getDescription());
        event.setLocation(location != null ? location : event.getLocation());
        event.setPaid(paid != null ? paid : event.isPaid());
        event.setCategory(categoryId != null ? getCategory(categoryId) : event.getCategory());
        event.setParticipantLimit(participantLimit != null ? participantLimit : event.getParticipantLimit());
        event.setRequestModeration(requestModeration != null ? requestModeration : event.isRequestModeration());
    }

    private void isExistsUser(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new BadRequestException(format(USER_NOT_EXISTS, userId));
        }
    }

    private void isExistsEvent(long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException(format(EVENT_NOT_EXISTS, eventId));
        }
    }

    private void isExistsUserLikeInitiatorEvent(long userId,
                                                long eventId) {
        isExistsUser(userId);
        isExistsEvent(eventId);
        if (!eventRepository.existsByIdAndInitiator_Id(eventId, userId)) {
            throw new BadRequestException(format("Event with id:(%d) " +
                    "and Initiator with id:(%d) not exist", eventId, userId));
        }
    }

    private User getUser(long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new BadRequestException(format(USER_NOT_EXISTS, userId)));
    }

    private Category getCategory(long id) {

        return categoryRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(format(CATEGORY_NOT_EXISTS, id)));
    }

    private Event getEvent(long eventId) {

        return eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new NotFoundException(format(EVENT_NOT_EXISTS, eventId)));
    }

    private Location getLocation(Location location) {
        if (location != null) {
            double lat = location.getLat();
            double lon = location.getLon();
            return locationRepository.getByLatAndLon(lat, lon)
                    .orElse(locationRepository.save(location));
        }
        return null;
    }

    private EventState getValidAdminEventState(StateAdminAction stateAction,
                                               EventState eventState) {
        String eventStateErrorMessage = "The event was moderated. " +
                "Current status of the event: %s";
        switch (stateAction) {
            case PUBLISH_EVENT:
                if (eventState != EventState.PENDING) {
                    throw new ForbiddenException(format(eventStateErrorMessage, eventState));
                }
                return EventState.PUBLISHED;
            case REJECT_EVENT:
                if (eventState == EventState.PUBLISHED) {
                    throw new ForbiddenException(format(eventStateErrorMessage, eventState));
                }
                return EventState.CANCELED;
            default:
                throw new ForbiddenException(format("Wrong status of" +
                        " the administrator's action: %s", stateAction));
        }
    }

    private Sort getSort(String sort) {
        sort = sort.toUpperCase();
        switch (EnumUtils.getEnum(EventSortState.class, sort)) {
            case VIEWS:
                return Sort.by("views").descending();
            case EVENT_DATE:
                return Sort.by("eventDate").descending();
            default:
                throw new BadRequestException("Wrong sorting parameters");
        }
    }

    private void validateConfirmation(Event event) {
        boolean isLimitlessRequests = event.getParticipantLimit() == 0;
        boolean isNoPreModeration = !event.isRequestModeration();
        if (isLimitlessRequests && isNoPreModeration) {
            throw new ForbiddenException("Confirmation of applications is not required " +
                    "for the event, the application limit is 0 " +
                    "or the pre-moderation of applications is disabled ");
        }
    }

    private void validateEventDate(LocalDateTime newEventDate) {
        int minutesBeforeModifiedForInitiator = 120;
        LocalDateTime timeLimit = LocalDateTime.now().plusMinutes(minutesBeforeModifiedForInitiator);
        if (newEventDate.isBefore(timeLimit)) {
            throw new ConflictException(format("Wrong event date: %td. " +
                    "Time limit: %td", newEventDate, timeLimit));
        }
    }

    /**
     * 409 - Conflict if event limit equals limit current time
     *
     * @param participantLimit event limit
     * @param limit            now limit
     */
    private void validateLimitRequests(int participantLimit,
                                       int limit) {
        if (participantLimit == limit) {
            throw new ConflictException("it is not possible to confirm the application " +
                    "if the limit on applications for this event has already been reached");
        }
    }

    private void validateRange(LocalDateTime rangeStart,
                               LocalDateTime rangeFinish) {
        String error;
        if (rangeFinish != null) {
            if (rangeFinish.isAfter(rangeStart)) {
                if (rangeFinish.equals(rangeStart)) {
                    error = format("Finish:%td equal Start:%td", rangeFinish, rangeStart);
                    throw new BadRequestException(error);
                }
            } else {
                error = format("Finish:%td before Start:%td", rangeFinish, rangeStart);
                throw new BadRequestException(error);
            }
        } else {
            error = "Finish is null";
            throw new BadRequestException(error);
        }
    }

    private void validateRequestStatus(EventRequestService.RequestState status) {
        if (!status.equals(PENDING)) {
            throw new ForbiddenException((format("Wrong request state: %s", status)));
        }
    }

    private void validateAdminTimeMoment(LocalDateTime eventDate) {
        String error;
        if (eventDate == null) {
            throw new ForbiddenException("eventDate is null");
        }

        int minutesBeforeModified = 60;
        if (LocalDateTime.now()
                .isBefore(eventDate.plusMinutes(minutesBeforeModified))) {
            error = format("Event date (%td) not before %d minutes", eventDate, minutesBeforeModified);
            throw new ForbiddenException(error);
        }
    }

    private SearchCriteria getCriteriaListUsers(List<Long> users) {
        if (users != null) {
            return new SearchCriteria(
                    "initiator",
                    userRepository.findAllById(users),
                    SearchOperation.IN
            );
        }
        return null;
    }

    private SearchCriteria getCriteriaListCategories(List<Long> categories) {
        if (categories != null) {
            return new SearchCriteria(
                    "category",
                    categoryRepository.findAllById(categories),
                    SearchOperation.IN
            );
        }
        return null;
    }

    private SearchCriteria getCriteriaPaid(Boolean paid) {
        if (paid != null) {

            return new SearchCriteria("paid", paid, SearchOperation.EQUAL);
        }

        return null;
    }

    private SearchCriteria getCriteriaRangeStart(LocalDateTime rangeStart) {
        if (rangeStart != null) {

            return new SearchCriteria("eventDate", rangeStart, SearchOperation.GREATER_THAN_EQUAL);
        }

        return null;
    }

    private SearchCriteria getCriteriaRangeFinish(LocalDateTime rangeFinish) {
        if (rangeFinish != null) {

            return new SearchCriteria("eventDate", rangeFinish, SearchOperation.LESS_THAN);
        }

        return null;
    }

    private SearchCriteria getCriteriaParticipantLimitNotDefined() {
        return new SearchCriteria("participantLimit", 0, SearchOperation.EQUAL);
    }

    private SearchCriteria getCriteriaStates(List<String> states) {
        if (states != null) {
            List<EventState> eventStates = new ArrayList<>();
            for (String state : states) {
                state = state.toUpperCase();
                if (EventState.isValid(state)) {
                    eventStates.add(EventState.valueOf(state));
                } else {
                    throw new BadRequestException("Wrong state");
                }
            }

            return new SearchCriteria("state", eventStates, SearchOperation.IN);
        }

        return null;
    }

    private SearchCriteria getCriteriaTextInTitle(String text) {

        return new SearchCriteria("title", text, SearchOperation.MATCH);
    }

    private SearchCriteria getCriteriaTextInAnnotation(String text) {

        return new SearchCriteria("annotation", text, SearchOperation.MATCH);
    }

    private SearchCriteria getCriteriaTextInDescription(String text) {

        return new SearchCriteria("description", text, SearchOperation.MATCH);
    }
}
