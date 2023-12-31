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
import ru.practicum.service.ClientService;
import ru.practicum.HitDto;
import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.service.ClientService.PATTERN;

@RestController
@RequiredArgsConstructor
public class ClientController {
    private final ClientService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitDto post(@RequestBody HitDto endpoint) {
        return service.post(endpoint);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> receive(
            @RequestParam @DateTimeFormat(pattern = PATTERN) LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = PATTERN) LocalDateTime end,
            @RequestParam(required = false) String[] uris,
            @RequestParam(defaultValue = "false") String unique) {
        return service.get(start, end, uris, unique);
    }
}
