package ru.practicum;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

//    protected ResponseEntity<Object> get(String path) {
//        return get(path, null, null);
//    }
//
//    protected ResponseEntity<Object> get(String path, Long id) {
//        return get(path, id, null);
//    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }

    protected ResponseEntity<Object> get(String path, Long id, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, id, parameters, null);
    }

//    protected <T> ResponseEntity<Object> post(String path, Long id, T body) {
//        return post(path, id, null, body);
//    }

    protected <T> ResponseEntity<Object> post(String path, T body) {
        return post(path, null, null, body);
    }
//    protected <T> ResponseEntity<Object> put(String path, Long id, T body) {
//        return put(path, id, null, body);
//    }
//
//    protected <T> ResponseEntity<Object> put(String path, Long id, @Nullable Map<String, Object> parameters, T body) {
//        return makeAndSendRequest(HttpMethod.PUT, path, id, parameters, body);
//    }

//    protected <T> ResponseEntity<Object> patch(String path, T body) {
//        return patch(path, null, null, body);
//    }
//
//    protected <T> ResponseEntity<Object> patch(String path, Long id) {
//        return patch(path, id, null, null);
//    }
//
//    protected <T> ResponseEntity<Object> patch(String path, Long id, T body) {
//        return patch(path, id, null, body);
//    }
//
//    protected <T> ResponseEntity<Object> patch(String path, Long id, @Nullable Map<String, Object> parameters, T body) {
//        return makeAndSendRequest(HttpMethod.PATCH, path, id, parameters, body);
//    }

//    protected ResponseEntity<Object> delete(String path) {
//        return delete(path, null, null);
//    }
//
//    protected ResponseEntity<Object> delete(String path, Long id) {
//        return delete(path, id, null);
//    }
//
//    protected ResponseEntity<Object> delete(String path, Long id, @Nullable Map<String, Object> parameters) {
//        return makeAndSendRequest(HttpMethod.DELETE, path, id, parameters, null);
//    }

    protected <T> ResponseEntity<Object> post(String path, Long id, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, id, parameters, body);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, Long id, @Nullable Map<String, Object> parameters, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders(id));

        ResponseEntity<Object> shareitServerResponse;
        try {
            if (parameters != null) {
                shareitServerResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                shareitServerResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(shareitServerResponse);
    }

    private HttpHeaders defaultHeaders(Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (id != null) {
            String header = "X-HEADER";
            headers.set(header, String.valueOf(id));
        }
        return headers;
    }
}
