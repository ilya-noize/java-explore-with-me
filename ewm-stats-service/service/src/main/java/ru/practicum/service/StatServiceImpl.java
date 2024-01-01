package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.HitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.Stats;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.StatRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.practicum.controller.StatController.PATTERN;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatServiceImpl implements StatService {
    private final StatRepository repository;
    private final StatMapper mapper;

    public HitDto saveForStatistic(HitDto dto) {
        log.info("[i] saveForStatistic \n dto = {}", dto);
        Stats entity = mapper.toEntity(dto);

        return mapper.toDto(repository.save(entity));
    }

    public List<ViewStatsDto> getStatistic(
            LocalDateTime start,
            LocalDateTime end,
            String[] uris,
            Boolean isUnique) {
        log.info("[i] getStatistic(unique = {}) \n start = {}, end = {}, uris = {}",
                isUnique, start, end, uris);
        boolean isEmptyUris = uris == null || uris.length == 0;
        List<ViewStats> showStatsFromDb;
        if (isUnique) {
            if (isEmptyUris) {
                showStatsFromDb = repository.getByCreatedBetweenAndUniqueIp(start, end);
            } else {
                showStatsFromDb = repository.getByCreatedBetweenAndUriInAndUniqueIp(start, end,
                        String.join(", ", uris));
            }
        } else {
            if (isEmptyUris) {
                showStatsFromDb = repository.getByCreatedBetween(start, end);
            } else {
                showStatsFromDb = repository.getByCreatedBetweenAndUriIn(start, end,
                        String.join(", ", uris));
            }
        }
        return showStatsFromDb.stream().map(this::toDto).collect(toList());
    }

    private ViewStatsDto toDto(ViewStats statisticData) {
        return ViewStatsDto.builder()
                .app(statisticData.getApp())
                .uri(statisticData.getUri())
                .hits(statisticData.getHits()).build();
    }

    private static class StatMapper {

        public Stats toEntity(HitDto dto) {
            LocalDateTime created = LocalDateTime.parse(dto.getTimestamp());

            return Stats.builder()
                    .id(dto.getId())
                    .app(dto.getApp())
                    .uri(dto.getUri())
                    .ip(dto.getIp())
                    .created(created).build();
        }

        public HitDto toDto(Stats entity) {
            String timestamp = entity.getCreated().format(DateTimeFormatter.ofPattern(PATTERN));

            return HitDto.builder()
                    .id(entity.getId())
                    .app(entity.getApp())
                    .uri(entity.getUri())
                    .ip(entity.getIp())
                    .timestamp(timestamp).build();
        }
    }
}
