package ru.practicum.mapper;


import org.springframework.stereotype.Component;
import ru.practicum.HitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.Stat;
import ru.practicum.model.ViewStats;

@Component
public class StatMapper {
    public Stat toEntity(HitDto dto) {

        return Stat.builder()
                .id(dto.getId())
                .app(dto.getApp())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .created(dto.getTimestamp()).build();
    }

    public HitDto toHitDto(Stat entity) {

        return HitDto.builder()
                .id(entity.getId())
                .app(entity.getApp())
                .uri(entity.getUri())
                .ip(entity.getIp())
                .timestamp(entity.getCreated()).build();
    }

    public ViewStatsDto toViewStatsDto(ViewStats statisticData) {

        return ViewStatsDto.builder()
                .app(statisticData.getApp())
                .uri(statisticData.getUri())
                .hits(statisticData.getHits()).build();
    }
}