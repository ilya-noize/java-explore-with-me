package ru.practicum.event.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.HitDto;
import ru.practicum.category.api.repository.CategoryRepository;
import ru.practicum.category.entity.Category;
import ru.practicum.constants.Constants;
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
import ru.practicum.event.request.entity.EventRequest;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.entity.Location;
import ru.practicum.location.mapper.LocationMapper;
import ru.practicum.location.repository.LocationRepository;
import ru.practicum.service.ClientService;
import ru.practicum.user.api.repository.UserRepository;
import ru.practicum.user.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static ru.practicum.constants.Constants.CATEGORY_NOT_EXISTS;
import static ru.practicum.constants.Constants.DATE_FORMAT;
import static ru.practicum.constants.Constants.EVENT_NOT_EXISTS;
import static ru.practicum.constants.Constants.RequestState.CONFIRMED;
import static ru.practicum.constants.Constants.RequestState.PENDING;
import static ru.practicum.constants.Constants.RequestState.REJECTED;
import static ru.practicum.constants.Constants.USER_NOT_EXISTS;
import static ru.practicum.constants.Constants.checkPageable;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRequestRepository requestRepository;
    private final LocationRepository locationRepository;
    private final ClientService statisticService;

    private static void validateEventState(Constants.EventState eventState) {
        if (eventState.equals(Constants.EventState.PUBLISHED)) {
            throw new ConflictException("Event is published.");
        }
    }

    @Override
    public EventDto create(long userId, NewEventDto dto) {
        // 400
        // 409 - Событие не удовлетворяет правилам создания
        User initiator = getUser(userId);
        Category category = getCategory(dto.getCategory());
        LocalDateTime eventDate = dto.getEventDate();
        validateEventDate(eventDate);

        Event event = EventMapper.INSTANCE.toEntity(dto);
        event.setEventDate(eventDate);
        event.setCategory(category);
        event.setInitiator(initiator);
        event.setState(Constants.EventState.PENDING);
        event.setLocation(getLocation(dto.getLocation()));
        event.setCreatedOn(LocalDateTime.now());
        Boolean paid = dto.getPaid();
        event.setPaid(paid != null && paid);
        Integer participantLimit = dto.getParticipantLimit();
        event.setParticipantLimit(participantLimit == null ? 0 : participantLimit);
        Boolean requestModeration = dto.getRequestModeration();
        event.setRequestModeration(requestModeration == null || requestModeration);

        return EventMapper.INSTANCE.toDto(
                eventRepository.save(event));
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

        return EventMapper.INSTANCE.toDto(event);
    }

    @Override
    public EventDto updateByInitializerAndId(Long userId, Long eventId, UpdateEventDto dto) {
        User initiator = getUser(userId);
        Event event = eventRepository.getByInitiatorAndId(initiator, eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_EXISTS));
        Constants.EventState eventState = event.getState();
        if (eventState.equals(Constants.EventState.PUBLISHED)) {
            throw new ConflictException("Can only change canceled events or events in the waiting state of moderation");
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

        Constants.StateAction stateAction = dto.getStateAction();
        if (stateAction != null) {
            switch (stateAction) {
                case SEND_TO_REVIEW:
                    event.setState(Constants.EventState.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(Constants.EventState.CANCELED);
                    break;
            }
        }

        return EventMapper.INSTANCE.toDto(
                eventRepository.save(event));
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
        int countConfirmedRequests = requestRepository.countByEvent_IdAndStatus(eventId, CONFIRMED);
        int eventParticipantLimit = event.getParticipantLimit();
        validateLimitRequests(eventParticipantLimit, countConfirmedRequests);

        EventRequestStatusUpdateResult statusUpdateResponse = new EventRequestStatusUpdateResult();
        Constants.RequestState updateStatus = eventRequestStatusUpdateRequest.getStatus();
        for (EventRequest request : requests) {

            Constants.RequestState status = request.getStatus();
//            validateRequestStatus(status);

            if (!status.equals(PENDING)) {
                throw new ConflictException((format("Wrong request state: %s", status)));
            }

            if (updateStatus.equals(CONFIRMED)) {
                countConfirmedRequests++;
                if (countConfirmedRequests <= eventParticipantLimit) {
                    request.setStatus(CONFIRMED);
                    statusUpdateResponse.getConfirmedRequests()
                            .add(EventRequestMapper.INSTANCE.toDto(request));
                } else {
//                    request.setStatus(REJECTED);
//                    statusUpdateResponse.getRejectedRequests()
//                            .add(EventRequestMapper.INSTANCE.toDto(request));
                    throw new ConflictException("It is not possible to confirm the request " +
                            "if the limit on requests for this event has already been reached");
                }
            } else {
                request.setStatus(REJECTED);
                statusUpdateResponse.getRejectedRequests()
                        .add(EventRequestMapper.INSTANCE.toDto(request));
            }
        }
        requestRepository.saveAll(requests);
        return statusUpdateResponse;
    }

    @Override
    public EventDto eventAdministration(Long eventId, UpdateEventAdminDto dto) {
        Event event = getEvent(eventId);
        validateAdminTimeMoment(dto.getEventDate());
        validateAdminTimeMoment(event.getEventDate());
        validateEventState(event.getState());
        Constants.StateAdminAction stateAction = dto.getStateAction();
        Constants.EventState eventState = event.getState();
        event.setState(getModifyEventState(stateAction, eventState));

        updateTitleAnnotationDescriptionCategoryLocationPaidParticipantLimitModeration(event,
                dto.getTitle(),
                dto.getAnnotation(),
                dto.getDescription(),
                dto.getCategory(),
                getLocation(dto.getLocation()),
                dto.getPaid(),
                dto.getParticipantLimit(),
                dto.getRequestModeration());

        return EventMapper.INSTANCE.toDto(eventRepository.save(event));
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
        log.info("\nusers:{},\nstates:{},\ncategories:{},\nrangeStart:{},\nrangeFinish:{},\nfrom:{},\nsize:{}",
                users, states, categories, rangeStart, rangeFinish, from, size);

        validateRange(rangeStart, rangeFinish);
        Pageable pageable = checkPageable(from, size, Sort.by(DESC, "id"));
        EventSpecification eventSpecification = new EventSpecification();

        if (isSizeNotZeroAndIndexZeroNotZero(users)) eventSpecification.add(getCriteriaListUsers(users));
        if (isSizeNotZeroAndIndexZeroNotZero(categories)) eventSpecification.add(getCriteriaListCategories(categories));
        if (states != null) eventSpecification.add(getCriteriaStates(states));
        if (rangeStart != null) eventSpecification.add(getCriteriaRangeStart(rangeStart));
        if (rangeFinish != null) eventSpecification.add(getCriteriaRangeFinish(rangeFinish));
        List<Event> events;
        boolean isEmptySpecs = eventSpecification.getList() == null;
        log.info("[i] get Events By Specification : {}. Empty? - {}", eventSpecification, isEmptySpecs);
        events = getEventsBySpecs(eventSpecification, pageable);

        return events.stream()
                .map(EventMapper.INSTANCE::toDto)
                .collect(toList());
    }

    /**
     * patch request query like <u>?users=0&categories=0</u>
     * <p>
     * boolean b1 = users == List.of(0L);   // false    <br/>
     * boolean b2 = !users.isEmpty();       // true     <br/>
     * boolean b3 = users.size() != 0;      // true     <br/>
     * boolean b4 = users.get(0) != 0;      // false    <br/>
     *
     * @param array Массив
     * @return Размер не ноль и первый элемент не ноль
     */
    private boolean isSizeNotZeroAndIndexZeroNotZero(List<Long> array) {
        if (array == null) return false;
        return array.size() != 0 && array.get(0) != 0;
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
        validateRange(rangeStart, rangeFinish);
        EventSpecification eventSpecification = new EventSpecification();
        if (text != null) {
            text = text.toUpperCase();
            eventSpecification.add(getCriteriaTextInTitle(text));
            eventSpecification.add(getCriteriaTextInAnnotation(text));
            eventSpecification.add(getCriteriaTextInDescription(text));
        }

        if (isSizeNotZeroAndIndexZeroNotZero(categories)) {
            eventSpecification.add(getCriteriaListCategories(categories));
        }
        if (paid != null) {
            eventSpecification.add(getCriteriaPaid(paid));
        }
        if (rangeStart != null) {
            eventSpecification.add(getCriteriaRangeStart(rangeStart));
        }
        if (rangeFinish != null) {
            eventSpecification.add(getCriteriaRangeFinish(rangeFinish));
        }
        if (onlyAvailable != null && onlyAvailable) {
            eventSpecification.add(getCriteriaParticipantLimitNotDefined());
        }
        List<Event> events = getEventsBySpecs(eventSpecification, pageable);

        addHit(httpServletRequest);
        events.forEach( event -> {
            long views = event.getViews() + 1;
            event.setViews(views);
        });
        eventRepository.saveAll(events);

        return events.stream()
                .map(EventMapper.INSTANCE::toShortDto)
                .collect(toList());
    }

    @Override
    public EventDto get(Long eventId, HttpServletRequest httpServletRequest) {
        Event event = getEvent(eventId);
        if (!event.getState().equals(Constants.EventState.PUBLISHED)) {
            throw new NotFoundException(format(EVENT_NOT_EXISTS, eventId));
        }
        addHit(httpServletRequest);
        long updatedViews = event.getViews() + 1;
        event.setViews(updatedViews);
        eventRepository.updateViews(eventId, updatedViews);

        return EventMapper.INSTANCE.toDto(event);
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

    private Location getLocation(LocationDto dto) {
        if (dto != null) {
            Location location = LocationMapper.INSTANCE.toEntity(dto);
            double lat = location.getLat();
            double lon = location.getLon();
            return locationRepository.getByLatAndLon(lat, lon)
                    .orElse(locationRepository.save(location));
        }
        return null;
    }

    private Constants.EventState getModifyEventState(StateAdminAction stateAction,
                                                     Constants.EventState eventState) {
        String eventStateErrorMessage = "The event was moderated. " +
                "Current status of the event: %s";
        if (stateAction == null) return Constants.EventState.PUBLISHED;
        switch (stateAction) {
            case PUBLISH_EVENT:
                if (eventState != Constants.EventState.PENDING) {
                    throw new ConflictException(format(eventStateErrorMessage, eventState));
                }
                return Constants.EventState.PUBLISHED;
            case REJECT_EVENT:
                if (eventState == Constants.EventState.PUBLISHED) {
                    throw new ConflictException(format(eventStateErrorMessage, eventState));
                }
                return Constants.EventState.CANCELED;
            default:
                throw new ForbiddenException(format("Wrong status of" +
                        " the administrator's action: %s", stateAction));
        }
    }

    private Sort getSort(String sort) {
        if (sort == null) return null;
        sort = sort.toUpperCase();
        switch (EnumUtils.getEnum(Constants.EventSortState.class, sort)) {
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
            throw new BadRequestException(format("Wrong event date: %td. " +
                    "Time limit: %td", newEventDate, timeLimit));
        }
    }

    /**
     * 409 - Conflict if event limit equals limit current time
     *
     * @param eventParticipantLimit event limit
     * @param countEventRequests            now limit
     */
    private void validateLimitRequests(int eventParticipantLimit,
                                       int countEventRequests) {
        if (eventParticipantLimit < countEventRequests && eventParticipantLimit != 0) {
            throw new ConflictException("it is not possible to confirm the request " +
                    "if the limit on requests for this event has already been reached");
        }
    }

    private void validateRange(LocalDateTime rangeStart,
                               LocalDateTime rangeFinish) {
        String error;
        if (rangeStart == null) rangeStart = LocalDateTime.now();
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
        }
    }

    private void validateAdminTimeMoment(LocalDateTime eventDate) {
        String error;
        if (eventDate != null) {
//            throw new ForbiddenException("eventDate is null");
//        }

            int minutesBeforeModified = 60;
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime timeLimit = now.minusMinutes(minutesBeforeModified);
            if (eventDate.isBefore(timeLimit)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
                String date = String.format(formatter.format(eventDate));
                error = format("Event date (%s) not before %d minutes", date, minutesBeforeModified);
                throw new BadRequestException(error);
            }
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
        return new SearchCriteria("paid", paid, SearchOperation.EQUAL);
    }

    private SearchCriteria getCriteriaRangeStart(LocalDateTime rangeStart) {

        return new SearchCriteria("eventDate", rangeStart, SearchOperation.GREATER_THAN_EQUAL);
    }

    private SearchCriteria getCriteriaRangeFinish(LocalDateTime rangeFinish) {
        return new SearchCriteria("eventDate", rangeFinish, SearchOperation.LESS_THAN);
    }

    private SearchCriteria getCriteriaParticipantLimitNotDefined() {
        return new SearchCriteria("participantLimit", 0, SearchOperation.EQUAL);
    }

    private SearchCriteria getCriteriaStates(List<String> states) {
        if (states != null) {
            List<Constants.EventState> eventStates = new ArrayList<>();
            for (String state : states) {
                state = state.toUpperCase();
                if (Constants.EventState.isValid(state)) {
                    eventStates.add(Constants.EventState.valueOf(state));
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
