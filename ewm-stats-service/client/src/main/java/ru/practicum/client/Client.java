package ru.practicum.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.HitDto;
import ru.practicum.ViewStatsDto;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

public class Client {
    public static final String HEADER = "X-Sharer-User-Id";
    protected final RestTemplate rest;

    public Client(RestTemplate rest) {
        this.rest = rest;
    }

    protected ResponseEntity<List<ViewStatsDto>> get(@Nullable Map<String, Object> parameters, List<ViewStatsDto> response) {
        return makeAndSendRequest(
                GET,
                "/stats?start={start}&end={end}&uris={uris}&uniq={unique}",
                parameters,
                null,
                response);
    }

    protected ResponseEntity<HitDto> post(HitDto body, HitDto response) {
        return makeAndSendRequest(
                POST,
                "/hit",
                null,
                body,
                response);
    }

    private <T, K> ResponseEntity<K> makeAndSendRequest(
            HttpMethod method,
            String path,
            @Nullable Map<String, Object> parameters,
            @Nullable T body,
            K response) {

        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders(null));
        Class kClass = response.getClass();
        ResponseEntity<K> shareitServerResponse;
        try {
            if (parameters != null) {
                shareitServerResponse = rest.exchange(
                        path,
                        method,
                        requestEntity,
                        kClass,
                        parameters);
            } else {
                shareitServerResponse = rest.exchange(
                        path,
                        method,
                        requestEntity,
                        kClass);
            }
        } catch (HttpStatusCodeException e) {
            return (ResponseEntity<K>) ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(shareitServerResponse);
    }

    private HttpHeaders defaultHeaders(Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (id != null) {
            headers.set(HEADER, String.valueOf(id));
        }
        return headers;
    }

    private static <T> ResponseEntity<T> prepareGatewayResponse(ResponseEntity<T> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}