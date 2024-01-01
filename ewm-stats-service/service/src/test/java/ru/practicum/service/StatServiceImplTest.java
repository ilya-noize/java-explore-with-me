package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.HitDto;
import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase
class StatServiceImplTest {
    private final StatService service;

    @Test
    void saveEndpoint() {
        String app = "ewm-main-service";
        String uri = "/main";
        String ip = "192.163.0.1";
        String timestamp = "2020-06-01 11:00:01";

        HitDto response = service.saveForStatistic(new HitDto(0L, app, uri, ip, timestamp));

        assertNotEquals(0L, response.getId());
    }

    @Test
    void getStatistic() {
        String app = "ewm-main-service";
        String uri = "/main";

        service.saveForStatistic(new HitDto(2L, app, uri, "192.163.0.1", "2020-06-01 11:00:01"));
        service.saveForStatistic(new HitDto(3L, app, uri, "192.163.0.1", "2020-06-01 11:11:11"));
        service.saveForStatistic(new HitDto(4L, app, uri, "192.163.0.2", "2020-06-01 11:59:59"));

        LocalDateTime start = LocalDateTime.of(2020, 6, 1, 10, 0, 0);
        LocalDateTime end = start.plusHours(2);

        List<ViewStatsDto> responseStat = service.getStatistic(start, end,new String[]{uri}, true);

        assertNotNull(responseStat);
        assertEquals(1, responseStat.size());
    }
}