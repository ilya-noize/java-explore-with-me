package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.service.StatService;

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
    private final String app = "ewm-main-service";
    private final String uri = "/main";
    private final String ip = "192.163.0.1";
    private final LocalDateTime[] timestamps = {
            LocalDateTime.of(2020, 6, 1, 11, 0, 1),
            LocalDateTime.of(2020, 6, 1, 11, 11, 11),
            LocalDateTime.of(2020, 6, 1, 11, 59, 59)
    };

    @Test
    void saveEndpoint() {
        HitDto request = new HitDto(0L, app, uri, ip, timestamps[0]);
        HitDto response = service.send(request);

        assertNotEquals(0L, response.getId());
    }

    @Test
    void getStatistic() {
        String ip2 = "192.163.0.2";

        service.send(new HitDto(2L, app, uri, ip, timestamps[0]));
        service.send(new HitDto(3L, app, uri, ip, timestamps[1]));
        service.send(new HitDto(4L, app, uri, ip2, timestamps[2]));

        LocalDateTime start = LocalDateTime.of(2020, 6, 1, 10, 0, 0);
        LocalDateTime end = start.plusHours(2);

        List<ViewStatsDto> responseStat = service.receive(start, end,new String[]{uri}, true);

        assertNotNull(responseStat);
        assertEquals(1, responseStat.size());
    }
}