package ru.practicum.dto.stats.get;

import lombok.Builder;

@Builder
public class StatsResponseDto {
    private long id;
    private String uri;
    private int hit;
}