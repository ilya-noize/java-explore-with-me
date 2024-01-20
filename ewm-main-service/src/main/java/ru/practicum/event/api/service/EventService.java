package ru.practicum.event.api.service;

import org.apache.commons.lang3.EnumUtils;
import ru.practicum.event.api.dto.EventDto;
import ru.practicum.event.api.dto.EventShortDto;
import ru.practicum.event.api.dto.NewEventDto;
import ru.practicum.event.api.dto.UpdateEventAdminDto;
import ru.practicum.event.api.dto.UpdateEventDto;
import ru.practicum.event.request.api.dto.EventRequestDto;
import ru.practicum.event.request.api.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.request.api.dto.EventRequestStatusUpdateResult;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    /**
     * Состояние события
     */
    enum EventState {
        PENDING,
        PUBLISHED,
        CANCELED;

        public static boolean isValid(String eventState) {
            return EnumUtils.isValidEnum(
                    EventState.class,
                    eventState.toUpperCase()
            );
        }
    }

    /**
     * Сортировка событий в поиске
     */
    enum EventSortState {
        EVENT_DATE,
        VIEWS
    }


    /**
     * Состояние события при обновлении
     */
    enum StateAction {
        SEND_TO_REVIEW,
        CANCEL_REVIEW
    }

    EventDto create(long userId, NewEventDto newEventDto);

    /**
     * PRIVATE
     *
     * @param userId ID user
     * @param from   Pageable
     * @param size   Pageable
     * @return List DTO events
     */
    List<EventShortDto> getByInitializer(Long userId, Integer from, Integer size);

    /**
     * PRIVATE
     *
     * @param userId  ID user
     * @param eventId ID event
     * @return DTO Event
     */
    EventDto getByInitializerAndId(Long userId, Long eventId);

    /**
     * PRIVATE
     * <p>
     * Обратите внимание:
     * <ul>
     *     <li>изменить можно только отмененные события
     *     или события в состоянии ожидания модерации (Ожидается код ошибки 409)</li>
     *     <li>дата и время на которые намечено событие не может быть раньше,
     *     чем через два часа от текущего момента (Ожидается код ошибки 409)</li>
     * </ul>
     *
     * @param userId         ID user
     * @param eventId        ID event
     * @param updateEventDto update event
     * @return DTO event
     */
    EventDto updateByInitializerAndId(Long userId, Long eventId, UpdateEventDto updateEventDto);

    /**
     * PRIVATE
     *
     * @param userId  ID user
     * @param eventId ID event
     * @return List DTO events
     */
    List<EventRequestDto> getRequestsInEvent(Long userId, Long eventId);

    /**
     * PRIVATE
     * <p>     * Обратите внимание:
     * <ul>
     *     <li>если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется</li>
     *     <li>нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409)</li>
     *     <li>статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409)</li>
     *     <li>если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить</li>
     * </ul>
     *
     * @param userId                          ID user
     * @param eventId                         ID event
     * @param eventRequestStatusUpdateRequest Update status event request
     * @return List DTO events
     */
    EventRequestStatusUpdateResult updateRequestsInEvent(
            Long userId,
            Long eventId,
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    /**
     * ADMIN
     * <p>
     * Редактирование данных любого события администратором. Валидация данных не требуется. Обратите внимание:
     * <ul>
     *     <li>дата начала изменяемого события должна быть не ранее чем за час от даты публикации. (Ожидается код ошибки 409)</li>
     *     <li>событие можно публиковать, только если оно в состоянии ожидания публикации (Ожидается код ошибки 409)</li>
     *     <li>событие можно отклонить, только если оно еще не опубликовано (Ожидается код ошибки 409)</li>
     * </ul>
     *
     * @param eventId             ID event
     * @param updateEventAdminDto update event
     * @return DTO event
     */
    EventDto eventAdministration(Long eventId, UpdateEventAdminDto updateEventAdminDto);

    /**
     * ADMIN
     *
     * @param users      List ID users
     * @param states     List events state
     * @param categories List ID categories
     * @param rangeStart start Date
     * @param rangeEnd   finish Date
     * @param from       From page
     * @param size       Pages
     * @return List DTO events
     */
    List<EventDto> getEventsAdministration(
            List<Long> users,
            List<String> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Integer from,
            Integer size);

    /**
     * PUBLIC
     * <p>     * Получение событий с возможностью фильтрации
     *
     * @param text          Поиск строки в названии, описании, анонсе
     * @param categories    ID Категории события
     * @param paid          Free or Pay
     * @param rangeStart    Начальный момент времени
     * @param rangeEnd      Конечный момент времени
     * @param onlyAvailable Доступные/Закрытые
     * @param sort          Сортировка по Дате или Просмотрам
     * @param from          From page
     * @param size          Pages
     * @return List DTO Events
     */
    List<EventShortDto> getAll(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            String sort,
            HttpServletRequest httpServletRequest,
            Integer from,
            Integer size);

    /**
     * PUBLIC
     * <p>      * Получение подробной информации об опубликованном событии по его идентификатору
     * <p>Обратите внимание:
     * <ul>
     *     <li>событие должно быть опубликовано</li>
     *     <li>информация о событии должна включать в себя количество просмотров и количество подтвержденных запросов</li>
     *     <li>информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики</li>
     * </ul>
     * <p>
     * В случае, если события с заданным id не найдено, возвращает статус код 404
     *
     * @param eventId     ID event
     * @param httpRequest http-data in statisticDB
     * @return DTO Event
     */
    EventDto get(Long eventId, HttpServletRequest httpRequest);
}
