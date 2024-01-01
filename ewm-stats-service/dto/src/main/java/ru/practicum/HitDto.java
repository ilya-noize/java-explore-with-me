package ru.practicum;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class HitDto {
    private long id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
