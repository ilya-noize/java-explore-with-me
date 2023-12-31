package ru.practicum;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <h3>Сохраняет данные о запросе по URL из App-приложения c IP-адреса пользователя в момент времени</h3>
 * {@link #id}     ID                              <br/>
 * {@link #app}    Запрос из какого приложения     <br/>
 * {@link #uri}    Запрос по какому URL            <br/>
 * {@link #ip}     Запрос с какого IP-адреса       <br/>
 * {@link #timestamp}  Когда произведён запрос     <br/>
 */

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HitDto {
    private long id;
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;
}
