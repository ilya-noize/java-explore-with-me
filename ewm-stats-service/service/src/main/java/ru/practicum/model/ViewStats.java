package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h3>Показывает количество запросов c разных IP-адресов к URL-адресу из App-приложения</h3>
 * {@link #app}     Имя сервиса         <br/>
 * {@link #uri}     URL запроса         <br/>
 * {@link #hits}    Счётчик запросов    <br/>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewStats {
    private String app;
    private String uri;
    private long hits;
}