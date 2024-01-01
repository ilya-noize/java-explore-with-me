package ru.practicum.mapper;


import org.springframework.stereotype.Component;
import ru.practicum.HitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.Stat;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.controller.StatController.PATTERN;

@Component
public class StatMapper {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN);

    public Stat toEntity(HitDto dto) {
        LocalDateTime created = LocalDateTime.parse(dto.getTimestamp(), dateTimeFormatter);

        return Stat.builder()
                .id(dto.getId())
                .app(dto.getApp())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .created(created).build();
    }

    public HitDto toHitDto(Stat entity) {
        String timestamp = dateTimeFormatter.format(entity.getCreated());

        return HitDto.builder()
                .id(entity.getId())
                .app(entity.getApp())
                .uri(entity.getUri())
                .ip(entity.getIp())
                .timestamp(timestamp).build();
    }

    public ViewStatsDto toViewStatsDto(ViewStats statisticData) {
        return ViewStatsDto.builder()
                .app(statisticData.getApp())
                .uri(statisticData.getUri())
                .hits(statisticData.getHits()).build();
    }
}