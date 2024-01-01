package ru.practicum;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.controller.StatController;
import ru.practicum.service.StatService;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = StatController.class)
@AutoConfigureMockMvc
class StatControllerTest {
    @MockBean
    private StatService service;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    private final String app = "ewm-main-service";
    private final String uri = "/main";
    private final String ip = "192.163.0.1";
    private final String timestamp = "2022-09-06 11:00:23";
    private final HitDto hitDto = new HitDto(1L, app, uri, ip, timestamp);

    @Test
    void send() throws Exception {
        when(service.send(any(HitDto.class)))
                .thenReturn(hitDto);

        String urlTemplate = "/hit";
        mvc.perform(post(urlTemplate)
                        .content(mapper.writeValueAsString(hitDto))
                        .characterEncoding(UTF_8)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.app").value(app))
                .andExpect(jsonPath("$.uri").value(uri))
                .andExpect(jsonPath("$.ip").value(ip))
                .andExpect(jsonPath("$.timestamp").value(timestamp));
    }

    @Test
    void receive() throws Exception {
        when(service.receive(any(), any(), any(), anyBoolean()))
                .thenReturn(List.of(new ViewStatsDto(app, uri, 1)));

        String urlTemplate = "/stats?start=2015-01-01 00:00:00&end=2045-01-01 00:00:00";
        mvc.perform(get(urlTemplate)
                        .characterEncoding(UTF_8)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].app").value(app))
                .andExpect(jsonPath("$.[0].uri").value(uri))
                .andExpect(jsonPath("$.[0].hits").value(1));
    }
}