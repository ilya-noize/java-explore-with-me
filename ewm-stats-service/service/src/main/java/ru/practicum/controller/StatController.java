package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.HitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatController {
    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final StatService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void send(@RequestBody HitDto dto) {
        service.saveForStatistic(dto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> receive(
            @RequestParam @DateTimeFormat(pattern = PATTERN) LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = PATTERN) LocalDateTime end,
            @RequestParam(required = false) String[] uris,
            @RequestParam(defaultValue = "false") Boolean unique) {

        return service.getStatistic(start, end, uris, unique);
    }
}
