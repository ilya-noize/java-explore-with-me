package ru.practicum.service;

import ru.practicum.HitDto;
import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    /**
     * <h3>Отправка данных для статистики</h3></h3>
     * @param dto Входящее тело запроса
     * @return Новая запись в статистике
     */
    HitDto send(HitDto dto);

    /**
     * <h3>Получение Статистики</h3>
     * @param start Начальное время поиска <br/>
     * @param end Конечное время поиска <br/>
     * @param uris Список запросов для поиска <br/>
     * @param isUnique Искать запросы с разных IP <br/>
     * @return Список отфильтрованных результатов <br/>
     */
    List<ViewStatsDto> receive(
            LocalDateTime start,
            LocalDateTime end,
            String[] uris,
            Boolean isUnique);
}
