package ru.practicum.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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

public class HttpClient {
    protected final RestTemplate rest;

    public HttpClient(RestTemplate rest) {
        this.rest = rest;
    }

    protected ResponseEntity<List<ViewStatsDto>> get(@Nullable Map<String, Object> parameters, List<ViewStatsDto> response) {
        return makeAndSendRequest(
                GET,
                "/stats?start={start}&end={end}&uris={uris}&unique={unique}",
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

        assert body != null;
        HttpEntity<T> requestEntity = new HttpEntity<>(body);
        Class kClass = response.getClass();
        ResponseEntity<K> serverResponse;
        try {
            if (parameters != null) {
                serverResponse = rest.exchange(
                        path,
                        method,
                        requestEntity,
                        kClass,
                        parameters);
            } else {
                serverResponse = rest.exchange(
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
        return prepareGatewayResponse(serverResponse);
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