package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.stats.get.StatsRequestDto;
import ru.practicum.dto.stats.get.StatsResponseDto;
import ru.practicum.dto.stats.post.RequestUserUrlDto;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Collections.emptyList;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final String timePattern = "yyyy-MM-dd HH:mm:ss";
    private final StatsService service;

    /**
     * Сохранение информации о том, что на uri конкретного сервиса был отправлен запрос пользователем.
     * Название сервиса, uri и ip пользователя указаны в теле запроса.
     * Вернуть 201 статус
     * @param dto dto
     */
    @PostMapping("/hits")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void save(RequestUserUrlDto dto) {
        log.debug("[i] Saving information that there was a request to the endpoint");

        service.save(dto);
    }

    /**
     * Получение статистики по посещениям.
     * @param start     Дата и время начала диапазона за который нужно выгрузить статистику
     * @param end       Дата и время конца диапазона за который нужно выгрузить статистику
     * @param uris      Список url для которых нужно выгрузить статистику
     * @param unique    Учесть только уникальные посещения (только с уникальным ip). по-умолчанию: false
     */
    @GetMapping("/stats")
    public List<StatsResponseDto> get(
            @RequestParam @DateTimeFormat(pattern = timePattern)
            LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = timePattern)
            LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique) {
        log.debug("[i] Getting statistics on visits.");

        StatsRequestDto requestDto = StatsRequestDto.builder()
                .start(start)
                .end(end)
                .uris((uris == null) ? emptyList() : uris)
                .unique(unique).build();

        return service.get(requestDto);
    }
}
