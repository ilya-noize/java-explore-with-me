package ru.practicum.moderate.api.service;

import ru.practicum.event.api.dto.EventDto;
import ru.practicum.moderate.api.dto.NewReviewDto;
import ru.practicum.moderate.api.dto.ReviewDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewService {
    /**
     * Сохранение нового ревью
     * @param eventId   Идентификатор события
     * @param dto       Новое ревью
     * @return Событие со списком ревью
     */
    EventDto save(long eventId, NewReviewDto dto);

    /**
     * Получить ревью по ID для события с указанным ID
     * @param eventId   Идентификатор события
     * @param reviewId  Идентификатор ревью
     * @return          Ревью
     */
    ReviewDto get(long eventId, long reviewId);

    /**
     * Получить список ревью для события по ID
     * @param eventId   Идентификатор события
     * @return  Список ревью для события по ID
     */
    List<ReviewDto> getAllByEvent(long eventId);

    /**
     * Получить список всех ревью по фильтрам
     * @param text          Упомянутый тест в причине и комментарии к ревью
     * @param states        Список статусов ревью
     * @param rangeStart    Начальная дата
     * @param rangeFinish   Конечная дата
     * @param sort          Выбранная сортировка результатов
     * @param from          Начальная запись в выборке
     * @param size          Количество записей в выборке
     * @return      Список всех ревью по фильтрам
     */
    List<ReviewDto> getAll(String text,
                           List<String> states,
                           LocalDateTime rangeStart,
                           LocalDateTime rangeFinish,
                           String sort,
                           Integer from,
                           Integer size);
}
