package ru.practicum.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.HitDto;
import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class StatisticService {
    private final String serverUrl = "http://stats-server:9090";
    private final RestTemplate restTemplate;

    public StatisticService() {
        this.restTemplate = new RestTemplate();
    }

    public void post(HitDto hitDto) {
        HttpEntity<HitDto> requestEntity = new HttpEntity<>(hitDto);
        restTemplate.exchange(serverUrl + "/hit",
                HttpMethod.POST,
                requestEntity,
                Object.class
        );
    }

    public ResponseEntity<ViewStatsDto[]> getData(LocalDateTime start,
                                                  LocalDateTime end,
                                                  String[] uris,
                                                  Boolean unique) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start);
        parameters.put("end", end);
        parameters.put("unique", unique);
        String path = serverUrl + "/stats?start={start}&end={end}&unique={unique}";
        if (uris != null) {
            parameters.put("uris", uris);
            path += "&uris={uris}";
        }

        ResponseEntity<ViewStatsDto[]> serverResponse = restTemplate.getForEntity(
                path,
                ViewStatsDto[].class,
                parameters
        );
        if (serverResponse.getStatusCode().is2xxSuccessful()) {
            return serverResponse;
        }
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(serverResponse.getStatusCode());
        if (serverResponse.hasBody()) {
            return responseBuilder.body(serverResponse.getBody());
        }
        return responseBuilder.build();
    }
}