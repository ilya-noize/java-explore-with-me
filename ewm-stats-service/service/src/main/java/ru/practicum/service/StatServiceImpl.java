package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.HitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.Stat;
import ru.practicum.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final StatRepository repository;

    public void saveEndpoint(HitDto dto) {
        Stat entity = Stat.builder()
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
        if (isUnique) {
            if (isEmptyUris) {
                return repository.getByCreatedBetweenAndUriInAndUniqueIp(start, end, String.join(", ", uris));
            } else {
                return repository.getByCreatedBetweenAndUniqueIp(start, end);
            }
        } else {
            if (isEmptyUris) {
                return repository.getByCreatedBetween(start, end);
            } else {
                return repository.getByCreatedBetweenAndUriIn(start, end, String.join(", ", uris));
            }
        }
    }
}
