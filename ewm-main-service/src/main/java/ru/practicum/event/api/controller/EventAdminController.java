package ru.practicum.event.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.event.api.dto.EventDto;
import ru.practicum.event.api.dto.UpdateEventAdminDto;
import ru.practicum.event.api.service.EventServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

import static prototype.Constants.FROM;
import static prototype.Constants.SIZE;


@Slf4j
public class EventAdminController extends EventController {
    public EventAdminController(EventServiceImpl service) {
        super(service);
    }

    /**
     * Редактирование данных любого события администратором. Валидация данных не требуется.
     * <p> Обратите внимание:
     * <ul>
     *  <li>дата начала изменяемого события должна быть не ранее чем за час от даты публикации. (Ожидается код ошибки 409)</li>
     *  <li>событие можно публиковать, только если оно в состоянии ожидания публикации (Ожидается код ошибки 409)</li>
     *  <li>событие можно отклонить, только если оно еще не опубликовано (Ожидается код ошибки 409)</li>
     * </ul>
     * @param eventId   ID event
     * @param updateEventAdminDto   update DTO event
     * @return  DTo event
     */
    @PatchMapping({"/admin/events/{eventId}"})
    public EventDto update(@PathVariable Long eventId, @RequestBody @Valid UpdateEventAdminDto updateEventAdminDto) {
        log.debug("[i][admin] update event ID:{}", eventId);

        return service.eventAdministration(eventId, updateEventAdminDto);
    }

    /**
     * Эндпоинт возвращает полную информацию обо всех событиях подходящих под переданные условия
     * <p>
     * В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
     * @param users         List ID users
     * @param states        List events state
     * @param categories    List ID categories
     * @param rangeStart    start Date
     * @param rangeEnd      finish Date
     * @param from          From page
     * @param size          Pages
     * @return  List DTO events
     */
    @GetMapping({"/admin/events"})
    public List<EventDto> getAll(
            @RequestParam List<Long> users,
            @RequestParam List<String> states,
            @RequestParam List<Long> categories,
            @RequestParam LocalDateTime rangeStart,
            @RequestParam LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = FROM) @Min(0) Integer from,
            @RequestParam(required = false, defaultValue = SIZE) @Min(1) Integer size) {
        log.debug("[i] events Administration\n" +
                "Search Param:\n" +
                "\tusers: {};\n\tstates:{}\n\tcategories:{}\n\trange:[{}-{}]\n\tPageable({}, {})",
                users, states, categories, rangeStart, rangeEnd, from, size);

        return service.getEventsAdministration(users, states, categories, rangeStart, rangeEnd, from, size);
    }
}
