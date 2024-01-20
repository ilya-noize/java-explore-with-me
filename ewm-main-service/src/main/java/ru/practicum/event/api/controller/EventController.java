package ru.practicum.event.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.api.dto.EventDto;
import ru.practicum.event.api.dto.EventShortDto;
import ru.practicum.event.api.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.constants.Constants.FROM;
import static ru.practicum.constants.Constants.SIZE;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EventController {
    protected final EventService service;

    /**
     * Получение событий с возможностью фильтрации
     * <p>Обратите внимание:
     * <ul>
     *     <li>это публичный эндпоинт, соответственно в выдаче должны быть только опубликованные события</li>
     *     <li>текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв</li>
     *     <li>если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события, которые произойдут позже текущей даты и времени</li>
     *     <li>информация о каждом событии должна включать в себя количество просмотров и количество уже одобренных заявок на участие</li>
     *     <li>информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики</li>
     * </ul>
     * В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
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
    @GetMapping({"/events"})
    public List<EventShortDto> getAll(
            @RequestParam String text,
            @RequestParam List<Long> categories,
            @RequestParam Boolean paid,
            @RequestParam LocalDateTime rangeStart,
            @RequestParam LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam String sort,
            HttpServletRequest httpServletRequest,
            @RequestParam(required = false, defaultValue = FROM) @Min(0) Integer from,
            @RequestParam(required = false, defaultValue = SIZE) @Min(1) Integer size) {
        log.debug("[i] get all events. Params: {\n" +
                        "\t text = {},\n" +
                        "\t categories = {},\n" +
                        "\t paid = {},\n" +
                        "\t rangeStart = {},\n" +
                        "\t rangeEnd = {},\n" +
                        "\t onlyAvailable = {},\n" +
                        "\t sort = {}\n}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort);

        return service.getAll(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, httpServletRequest,
                from, size);
    }


    @GetMapping({"/events/{eventId}"})
    public EventDto get(@PathVariable Long eventId, HttpServletRequest httpRequest) {
        log.debug("[i] get event ID:{}", eventId);
        return service.get(eventId, httpRequest);
    }
}
