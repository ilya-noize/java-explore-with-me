package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.stats.get.StatsRequestDto;
import ru.practicum.dto.stats.get.StatsResponseDto;
import ru.practicum.dto.stats.post.RequestUserUrlDto;
import ru.practicum.entity.Stats;
import ru.practicum.entity.StatsHits;
import ru.practicum.repository.StatsRepository;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final StatsRepository repository;

    public void save(RequestUserUrlDto dto) {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            repository.save(Stats.builder()
                    .app(dto.getApp())
                    .uri(dto.getUri())
                    .ip(ip)
                    .created(LocalDateTime.now())
                    .build());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public List<StatsResponseDto> get(StatsRequestDto dto) {
        LocalDateTime start = dto.getStart();
        LocalDateTime end = dto.getEnd();
        List<String> uris = dto.getUris();
        boolean isEmptyUris = uris.isEmpty();
        String urisString = String.join(", ", uris);
        List<StatsHits> response;
        if (dto.isUnique()) {
            if (isEmptyUris) {
                response = repository.getByCreatedBetweenAndUriInAndUniqueIp(start, end, urisString);
            } else {
                response = repository.getByCreatedBetweenAndUniqueIp(start, end);
            }
        } else {
            if (isEmptyUris) {
                response = repository.getByCreatedBetween(start, end);
            } else {
                response = repository.getByCreatedBetweenAndUriIn(start, end, urisString);
            }
        }

        List<StatsResponseDto> responseDtos = new ArrayList<>();
        response.forEach(statsHits ->
                responseDtos.add(
                        StatsResponseDto.builder()
                                .id(statsHits.getId())
                                .uri(statsHits.getUri())
                                .hit(statsHits.getHit()).build()));

        return responseDtos;
    }
}
