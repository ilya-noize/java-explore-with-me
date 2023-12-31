package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.HitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.client.Client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ClientService extends Client {
    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String API_PREFIX = "/";

    @Autowired
    public ClientService(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(
                        new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public HitDto post(@RequestBody HitDto dto) {
        ResponseEntity<HitDto> responseEntity = this.post(dto, new HitDto());
        return responseEntity.getBody();
    }

    public List<ViewStatsDto> get(LocalDateTime start, LocalDateTime end, String[] uris, String unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
        Map<String, Object> map = Map.of(
                "start", start.format(formatter),
                "end", end.format(formatter),
                "uris", uris,
                "unique", unique

        );
        ResponseEntity<List<ViewStatsDto>> responseEntity =
                this.get(map, new ArrayList<>());

        return responseEntity.getBody();
    }
}
