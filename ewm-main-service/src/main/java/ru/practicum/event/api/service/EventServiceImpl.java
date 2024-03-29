package ru.practicum.event.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.HitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.category.api.repository.CategoryRepository;
import ru.practicum.category.entity.Category;
import ru.practicum.event.api.dto.EventDto;
import ru.practicum.event.api.dto.EventShortDto;
import ru.practicum.event.api.dto.NewEventDto;
import ru.practicum.event.api.dto.UpdateEventAdminDto;
import ru.practicum.event.api.dto.UpdateEventDto;
import ru.practicum.event.api.mapper.EventMapper;
import ru.practicum.event.api.repository.EventRepository;
import ru.practicum.event.entity.Event;
import ru.practicum.event.entity.EventSortState;
import ru.practicum.event.entity.EventState;
import ru.practicum.event.entity.RequestState;
import ru.practicum.event.entity.StateAction;
import ru.practicum.event.entity.StateAdminAction;
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
import java.util.Objects;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ObjectUtils.allNotNull;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static ru.practicum.constants.Constants.CATEGORY_NOT_EXISTS;
import static ru.practicum.constants.Constants.DATE_FORMAT;
import static ru.practicum.constants.Constants.EVENT_NOT_EXISTS;
import static ru.practicum.constants.Constants.USER_NOT_EXISTS;
import static ru.practicum.constants.Constants.checkPageable;
import static ru.practicum.event.entity.EventState.PUBLISHED;
import static ru.practicum.event.entity.RequestState.CONFIRMED;
import static ru.practicum.event.entity.RequestState.PENDING;
import static ru.practicum.event.entity.RequestState.REJECTED;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private static final String EVENT_DATE = "eventDate";
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRequestRepository requestRepository;
    private final LocationRepository locationRepository;
    private final ClientService statisticService;
    private final EventMapper eventMapper;
    private final EventRequestMapper eventRequestMapper;
    private final LocationMapper locationMapper;


    private static void validateEventState(EventState eventState) {
        if (eventState.equals(PUBLISHED)) {
            throw new ConflictException("Event is published.");
        }
    }

    @Override
    public EventDto create(long userId, NewEventDto dto) {
        User initiator = getUser(userId);
        Category category = getCategory(dto.getCategory());
        LocalDateTime eventDate = dto.getEventDate();
        validateEventDate(eventDate);

        Event event = eventMapper.toEntity(dto);
        event.setEventDate(eventDate);
        event.setCategory(category);
        event.setInitiator(initiator);
        event.setState(EventState.PENDING);
        event.setLocation(getLocation(dto.getLocation()));
        event.setCreatedOn(LocalDateTime.now());
        Boolean paid = dto.getPaid();
        event.setPaid(paid != null && paid);
        Integer participantLimit = dto.getParticipantLimit();
        event.setParticipantLimit(participantLimit == null ? 0 : participantLimit);
        Boolean requestModeration = dto.getRequestModeration();
        event.setRequestModeration(requestModeration == null || requestModeration);
        event.setConfirmedRequests(0);

        return eventMapper.toDto(
                eventRepository.save(event));
    }

    @Override
    public List<EventShortDto> getByInitializer(Long userId, Integer from, Integer size) {
        Pageable pageable = checkPageable(from, size, null);
        User initiator = getUser(userId);
        List<Event> events = eventRepository.getByInitiator(initiator, pageable)
                .orElse(emptyList());

        return events.stream()
                .map(eventMapper::toShortDto)
                .collect(toList());
    }

    @Override
    public EventDto getByInitializerAndId(Long userId, Long eventId) {
        User initiator = getUser(userId);
        Event event = eventRepository.getByInitiatorAndId(initiator, eventId)
                .orElseThrow(() ->
                        new NotFoundException(format(EVENT_NOT_EXISTS, eventId)));

        return eventMapper.toDto(event);
    }

    @Override
    public EventDto updateByInitializerAndId(Long userId, Long eventId, UpdateEventDto dto) {
        User initiator = getUser(userId);
        Event event = eventRepository.getByInitiatorAndId(initiator, eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_EXISTS));
        EventState eventState = event.getState();
        if (eventState.equals(PUBLISHED)) {
            throw new ConflictException("Can only change canceled events or events in the waiting state of moderation");
        }

        LocalDateTime newEventDate = dto.getEventDate();
        if (newEventDate != null) {
            validateEventDate(newEventDate);
            event.setEventDate(newEventDate);
        }

        updateTitleAnnotDescrCategoryLocationPaidParLimModer(event, toAdminDto(dto));

        StateAction stateAction = dto.getStateAction();
        if (stateAction != null) {
            if (stateAction == StateAction.SEND_TO_REVIEW) {
                event.setState(EventState.PENDING);
            } else {
                event.setState(EventState.CANCELED);
            }
        }

        return eventMapper.toDto(
                eventRepository.save(event));
    }

    @Override
    public List<EventRequestDto> getRequestsInEvent(Long userId, Long eventId) {
        isExistsUserLikeInitiatorEvent(userId, eventId);
        return requestRepository.getByEvent_Id(eventId).stream()
                .map(eventRequestMapper::toDto)
                .collect(toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestsInEvent(
            Long userId,
            Long eventId,
            EventRequestStatusUpdateRequest dto) {
        isExistsUser(userId);
        Event event = getEvent(eventId);
        validateConfirmation(event);
        List<Long> requestIds = dto.getRequestIds();
        List<EventRequest> requests = requestRepository.findAllById(requestIds);
        int countConfirmedRequests = requestRepository.countByEvent_IdAndStatus(eventId, CONFIRMED);
        int eventParticipantLimit = event.getParticipantLimit();
        validateLimitRequests(eventParticipantLimit, countConfirmedRequests);

        EventRequestStatusUpdateResult statusUpdateResponse = new EventRequestStatusUpdateResult();
        RequestState updateStatus = dto.getStatus();
        for (EventRequest request : requests) {

            RequestState status = request.getStatus();

            if (!status.equals(PENDING)) {
                throw new ConflictException((format("Wrong request state: %s", status)));
            }

            if (updateStatus.equals(CONFIRMED)) {
                countConfirmedRequests++;
                if (countConfirmedRequests <= eventParticipantLimit) {
                    request.setStatus(CONFIRMED);
                    statusUpdateResponse.getConfirmedRequests()
                            .add(eventRequestMapper.toDto(request));
                    eventRepository.updateConfirmedRequestsById(
                            countConfirmedRequests,
                            eventId);
                } else {
                    throw new ConflictException("It is not possible to confirm the request " +
                            "if the limit on requests for this event has already been reached");
                }
            } else {
                request.setStatus(REJECTED);
                statusUpdateResponse.getRejectedRequests()
                        .add(eventRequestMapper.toDto(request));
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
        StateAdminAction stateAction = dto.getStateAction();
        EventState eventState = event.getState();
        EventState modifyEventState = getModifyEventState(stateAction, eventState);
        event.setState(modifyEventState);
        if (modifyEventState.equals(PUBLISHED)) {
            event.setPublishedOn(LocalDateTime.now());
        }

        updateTitleAnnotDescrCategoryLocationPaidParLimModer(event, dto);

        return eventMapper.toDto(eventRepository.save(event));
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
        log.debug("[i] get Events Administration\nusers:{},\nstates:{},\ncategories:{},\nrangeStart:{},\nrangeFinish:{},\nfrom:{},\nsize:{}",
                users, states, categories, rangeStart, rangeFinish, from, size);

        validateRange(rangeStart, rangeFinish);
        Pageable pageable = checkPageable(from, size, Sort.by(DESC, "id"));

        List<Event> events;
        if (allNotNull(users, states, categories, rangeStart, rangeFinish)) {
            events = getEventsBySearchSpecification(
                    getSearchAdminSpecification(users, states, categories, rangeStart, rangeFinish),
                    pageable);
        } else events = eventRepository.findAll(pageable);

        return events.stream()
                .map(eventMapper::toDto)
                .collect(toList());
    }

    @Override
    public EventDto get(Long eventId, HttpServletRequest httpServletRequest) {
        Event event = getEvent(eventId);
        if (!event.getState().equals(PUBLISHED)) {
            throw new NotFoundException(format(EVENT_NOT_EXISTS, eventId));
        }
        addHit(httpServletRequest);
        long uniqueViews = getViews(eventId, true);
        event.setViews(uniqueViews);

        return eventMapper.toDto(event);
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

        List<Event> events = getEventsBySearchSpecification(
                getSearchSpecification(text, categories, rangeStart, rangeFinish, paid, onlyAvailable),
                pageable);

        addHit(httpServletRequest);
        events.forEach(event -> {
            Long eventId = event.getId();
            long uniqueViews = getViews(eventId, false);
            event.setViews(uniqueViews);
            updateViewsByIdEvent(eventId, uniqueViews);
        });
        eventRepository.saveAll(events);

        return events.stream()
                .map(eventMapper::toShortDto)
                .collect(toList());
    }

    private UpdateEventAdminDto toAdminDto(UpdateEventDto dto) {
        return UpdateEventAdminDto.builder()
                .title(dto.getTitle())
                .annotation(dto.getAnnotation())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .location(dto.getLocation())
                .paid(dto.getPaid())
                .eventDate(dto.getEventDate())
                .participantLimit(dto.getParticipantLimit())
                .requestModeration(dto.getRequestModeration())
                .stateAction(null).build();
    }

    private List<Specification<Event>> addCategoriesAndRangePeriod(List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeFinish) {
        List<Specification<Event>> specifications = new ArrayList<>();
        if (categories != null) {
            List<Category> categoriesList = categories.stream()
                    .map(id -> categoryRepository.findById(id).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(toList());
            specifications.add(((root, query, criteriaBuilder) -> criteriaBuilder
                    .in(root.get("category")).value(categoriesList)));
        }
        specifications.add(((root, query, criteriaBuilder) -> criteriaBuilder
                .greaterThanOrEqualTo(root.get(EVENT_DATE), rangeStart)));
        if (rangeFinish != null) {
            specifications.add(((root, query, criteriaBuilder) -> criteriaBuilder
                    .lessThan(root.get(EVENT_DATE), rangeFinish)));
        }

        return specifications;
    }

    private List<Specification<Event>> getSearchAdminSpecification(List<Long> users,
                                                                   List<String> states,
                                                                   List<Long> categories,
                                                                   LocalDateTime rangeStart,
                                                                   LocalDateTime rangeFinish) {
        List<Specification<Event>> specifications = addCategoriesAndRangePeriod(categories, rangeStart, rangeFinish);

        if (users != null) {
            List<User> userList = userRepository.findAllById(users);
            if (!userList.isEmpty()) {
                specifications.add(((root, query, criteriaBuilder) -> criteriaBuilder
                        .in(root.get("initiator")).value(userList)));
            }
        }

        if (states != null) {
            specifications.add(((root, query, criteriaBuilder) -> criteriaBuilder
                    .in(root.get("state")).value(getEventStateList(states))));
        }

        return specifications;
    }

    private List<EventState> getEventStateList(List<String> states) {
        List<EventState> eventStates = new ArrayList<>();
        for (String state : states) {
            state = state.toUpperCase();
            if (EventState.isValid(state)) {
                eventStates.add(EventState.valueOf(state));
            }
        }

        return eventStates;
    }

    private List<Specification<Event>> getSearchSpecification(String text,
                                                              List<Long> categories,
                                                              LocalDateTime rangeStart,
                                                              LocalDateTime rangeFinish,
                                                              Boolean paid,
                                                              Boolean onlyAvailable) {
        List<Specification<Event>> specifications = addCategoriesAndRangePeriod(categories, rangeStart, rangeFinish);
        if (text != null) {
            specifications.add(((root, query, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.like(
                                    criteriaBuilder.upper(
                                            root.get("annotation")),
                                    "%" + text.toUpperCase() + "%"),
                            criteriaBuilder.like(
                                    criteriaBuilder.upper(
                                            root.get("description")),
                                    "%" + text.toUpperCase() + "%"))));
        }

        if (paid != null) {
            if (paid) {
                specifications.add((root, query, criteriaBuilder) -> criteriaBuilder
                        .isTrue(root.get("paid")));
            } else {
                specifications.add((root, query, criteriaBuilder) -> criteriaBuilder
                        .isFalse(root.get("paid")));
            }
        }

        if (onlyAvailable != null && onlyAvailable) {
            specifications.add(((root, query, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.equal(
                                    root.get("participantLimit"), 0),
                            criteriaBuilder.lessThan(
                                    root.get("confirmedRequests"),
                                    root.get("participantLimit")))
            ));
        }

        specifications.add(((root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("state"), PUBLISHED)));

        return specifications;
    }

    private List<Event> getEventsBySearchSpecification(List<Specification<Event>> specifications, Pageable pageable) {
        Specification<Event> eventSpecification = specifications.stream()
                .filter(Objects::nonNull)
                .reduce(Specification::and)
                .orElse(null);
        return eventRepository.findAll(eventSpecification, pageable).toList();
    }

    private void updateViewsByIdEvent(Long eventId, long uniqueViews) {
        if (eventRepository.updateViewsById(uniqueViews, eventId) > 0) {
            log.debug("[✓] update statistic data");
        } else log.warn("[!] fail update statistic data");
    }

    private void addHit(HttpServletRequest httpServletRequest) {

        statisticService.post(HitDto.builder()
                .app("ewm-main-service")
                .uri(httpServletRequest.getRequestURI())
                .ip(httpServletRequest.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());
    }

    private long getViews(long eventId, boolean unique) {
        List<ViewStatsDto> response = statisticService.get(
                LocalDateTime.now().minusYears(1),
                LocalDateTime.now(),
                new String[]{"/events/" + eventId},
                unique);

        return !response.isEmpty() ? response.get(0).getHits() : 0;
    }

    private void updateTitleAnnotDescrCategoryLocationPaidParLimModer(
            Event event,
            UpdateEventAdminDto dto) {
        String title = dto.getTitle();
        String annotation = dto.getAnnotation();
        String description = dto.getDescription();
        Long categoryId = dto.getCategory();
        Location location = getLocation(dto.getLocation());
        Boolean paid = dto.getPaid();
        Integer participantLimit = dto.getParticipantLimit();
        Boolean requestModeration = dto.getRequestModeration();

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
            Location location = locationMapper.toEntity(dto);
            double lat = location.getLat();
            double lon = location.getLon();
            return locationRepository.getByLatAndLon(lat, lon)
                    .orElse(locationRepository.save(location));
        }
        return null;
    }

    private EventState getModifyEventState(StateAdminAction stateAction,
                                           EventState eventState) {
        String eventStateErrorMessage = "The event was moderated. " +
                "Current status of the event: %s";
        if (stateAction == null) return PUBLISHED;
        switch (stateAction) {
            case PUBLISH_EVENT:
                if (eventState != EventState.PENDING) {
                    throw new ConflictException(format(eventStateErrorMessage, eventState));
                }
                return PUBLISHED;
            case REJECT_EVENT:
                if (eventState == PUBLISHED) {
                    throw new ConflictException(format(eventStateErrorMessage, eventState));
                }
                return EventState.CANCELED;
            default:
                throw new ForbiddenException(format("Wrong status of" +
                        " the administrator's action: %s", stateAction));
        }
    }

    private Sort getSort(String sort) {
        if (sort == null) return null;
        EventSortState anEnum = EnumUtils.getEnum(
                EventSortState.class, sort.toUpperCase());
        switch (anEnum) {
            case VIEWS:
                return Sort.by("views").descending();
            case EVENT_DATE:
                return Sort.by(EVENT_DATE).descending();
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
     * @param countEventRequests    now limit
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
}
