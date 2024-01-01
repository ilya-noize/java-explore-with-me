package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * <h3>Показывает количество запросов c разных IP-адресов к URL-адресу из App-приложения</h3>
 * {@link #app}     Имя сервиса         <br/>
 * {@link #uri}     URL запроса         <br/>
 * {@link #hits}    Счётчик запросов    <br/>
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewStatsDto {
    private String app;
    private String uri;
    private long hits;
}
