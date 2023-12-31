package ru.practicum.service;

import ru.practicum.HitDto;
import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    void saveEndpoint(HitDto dto);

    List<ViewStatsDto> getStatistic(
            LocalDateTime start,
            LocalDateTime end,
            String[] uris,
            Boolean isUnique);
}
