package ru.practicum.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.HitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.client.HttpClient;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ClientService extends HttpClient {
    private static final String API_PREFIX = "/";

    @Autowired
    public ClientService(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(
                        new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public void post(HitDto dto) {
        log.debug("[client] save statistic data by URL:{}\nIP:{}\nAPP:{}",
                dto.getUri(), dto.getId(), dto.getApp());
        this.post(dto, new HitDto());
    }

    public List<ViewStatsDto> get(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        if (start.isAfter(end)) throw new DateTimeException("start after end!");

        log.debug("[client] get statistic data by URLs:{}\nPeriod:{} - {}", uris, start, end);
        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String startFormat = start.format(formatter);
        String endFormat = end.format(formatter);

        Map<String, Object> parameters = Map.of(
                "start", startFormat,
                "end", endFormat,
                "uris", String.join(",", uris),
                "unique", unique

        );

        ResponseEntity<List<ViewStatsDto>> response = this.get(parameters, new ArrayList<>());

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                log.debug("[]");
                return List.of(mapper.readValue(
                        mapper.writeValueAsString(response.getBody()),
                        ViewStatsDto[].class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return null;
    }
}