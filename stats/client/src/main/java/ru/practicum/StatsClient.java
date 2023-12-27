package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.stats.post.RequestUserUrlDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatsClient extends BaseClient {
    private final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss");
    @Value("${server.application.name:ewm-main-service}")
    private String applicationName;

    public StatsClient(
            @Value("${shareit-server.url}") String serverUrl,
            RestTemplateBuilder builder) {

        super(builder
                .uriTemplateHandler(
                        new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity<Object> save(HttpServletRequest request) {
        final RequestUserUrlDto requestUserUrlDto = RequestUserUrlDto.builder()
                .app(applicationName)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();

        return post("/hit", requestUserUrlDto);
    }

    public ResponseEntity<Object> get(LocalDateTime start,
                                      LocalDateTime end,
                                      List<String> uris,
                                      Boolean unique) {
        Map<String, Object> parameters = new HashMap<>(Map.of(
                "start", start.format(formatter),
                "end", end.format(formatter)
        ));

        if (uris != null) {
            parameters.put("uris", String.join(",", uris));
        }
        if (unique) {
            parameters.put("unique", true);
        }

        return get("/stats?start={start}&end={end}", null, parameters);
    }
}