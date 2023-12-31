package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.HitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.ShowStats;
import ru.practicum.model.Stats;
import ru.practicum.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final StatRepository repository;

    public void saveEndpoint(HitDto dto) {
        Stats entity = Stats.builder()
                .app(dto.getApp())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .created(dto.getTimestamp()).build();
        repository.save(entity);
    }

    public List<ViewStatsDto> getStatistic(
            LocalDateTime start,
            LocalDateTime end,
            String[] uris,
            Boolean isUnique) {

        boolean isEmptyUris = uris == null || uris.length == 0;
        List<ShowStats> showStatsFromDb;
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
        return showStatsFromDb.stream()
                .map(showStats -> ViewStatsDto.builder()
                        .app(showStats.getApp())
                        .uri(showStats.getUri())
                        .hits(showStats.getHits()).build())
                .collect(toList());
    }
}
