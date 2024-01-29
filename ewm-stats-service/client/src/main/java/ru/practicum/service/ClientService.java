package ru.practicum.service;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
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
        this.post(dto, new HitDto());
    }

    public ResponseEntity<List<ViewStatsDto>> get(String start, String end, String[] uris, boolean unique) {
        Map<String, Object> map = Map.of(
                "start", start,
                "end", end,
                "uris", String.join(",", uris),
                "unique", unique

        );

        return this.get(map, new ArrayList<>());
    }
}