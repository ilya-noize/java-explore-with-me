package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.HitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.mapper.StatMapper;
import ru.practicum.model.Stat;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.StatRepository;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatServiceImpl implements StatService {
    private final StatRepository repository;
    private final StatMapper mapper;

    public HitDto send(HitDto dto) {
        log.info("[i] saveForStatistic \n dto = {}", dto);
        Stat entity = mapper.toEntity(dto);

        return mapper.toHitDto(repository.save(entity));
    }

    public List<ViewStatsDto> receive(LocalDateTime start,
                                      LocalDateTime end,
                                      String[] uris,
                                      Boolean isUnique) {
        if (isUnique == null) isUnique = false;
        log.info("[i] getStatistic(unique = {}) \n start = {}, end = {}, uris = {}",
                isUnique, start, end, uris);
        boolean isEmptyUris = uris == null || uris.length == 0;

        if (start.isAfter(end)) throw new DateTimeException("start after end!");

        List<ViewStats> showStatsFromDb;
        if (isUnique) {
            if (isEmptyUris) {
                showStatsFromDb = repository.getByCreatedBetweenAndUniqueIp(start, end,
                        Sort.by("hits").descending());
            } else {
                showStatsFromDb = repository.getByCreatedBetweenAndUriInAndUniqueIp(start, end, uris,
                        Sort.by("hits").descending());
            }
        } else {
            if (isEmptyUris) {
                showStatsFromDb = repository.getByCreatedBetween(start, end,
                        Sort.by("created").descending());
            } else {
                showStatsFromDb = repository.getByCreatedBetweenAndUriIn(start, end, uris,
                        Sort.by("created").descending());
            }
        }
        return showStatsFromDb.stream()
                .map(mapper::toViewStatsDto)
                .collect(toList());
    }
}
