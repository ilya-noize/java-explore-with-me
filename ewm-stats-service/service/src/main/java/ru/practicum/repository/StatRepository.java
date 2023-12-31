package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Stat, Long> {
    /**
     * @param createdStart  Дата и время начала диапазона за который нужно выгрузить статистику
     * @param createdEnd    Дата и время конца диапазона за который нужно выгрузить статистику
     * @param uris          Список url для которых нужно выгрузить статистику
     * @return      Список всех url с количеством запросов
     */
    @Query(value = "SELECT id, app, uri, COUNT(ip) hit, created FROM stat " +
            "WHERE (created >= :createdStart AND created <= :createdEnd)" +
            " AND s.uri IN (:uris)",
            nativeQuery = true)
    List<ViewStatsDto> getByCreatedBetweenAndUriIn(
            @Param("createdStart") LocalDateTime createdStart,
            @Param("createdEnd") LocalDateTime createdEnd,
            @Param("uris") String uris);

    /**
     * @param createdStart  Дата и время начала диапазона за который нужно выгрузить статистику
     * @param createdEnd    Дата и время конца диапазона за который нужно выгрузить статистику
     * @return      Список всех запросов
     */
    @Query(value = "SELECT id, app, uri, COUNT(ip) hit, created FROM STAT " +
            "WHERE (created >= :createdStart AND created <= :createdEnd)",
            nativeQuery = true)
    List<ViewStatsDto> getByCreatedBetween(
            @Param("createdStart") LocalDateTime createdStart,
            @Param("createdEnd") LocalDateTime createdEnd);

    /**
     * @param createdStart  Дата и время начала диапазона за который нужно выгрузить статистику
     * @param createdEnd    Дата и время конца диапазона за который нужно выгрузить статистику
     * @param uris          Список url для которых нужно выгрузить статистику
     * @return      Список всех url с количеством уникальных запросов
     */
    @Query(value = "SELECT id, app, uri, COUNT(DISTINCT ip) hit, created FROM STAT " +
            "WHERE (created >= :createdStart AND created <= :createdEnd)" +
            " AND s.uri IN (:uris)",
            nativeQuery = true)
    List<ViewStatsDto> getByCreatedBetweenAndUriInAndUniqueIp(
            @Param("createdStart") LocalDateTime createdStart,
            @Param("createdEnd") LocalDateTime createdEnd,
            @Param("uris") String uris);

    /**
     * @param createdStart  Дата и время начала диапазона за который нужно выгрузить статистику
     * @param createdEnd    Дата и время конца диапазона за который нужно выгрузить статистику
     * @return      Список всех уникальных запросов
     */
    @Query(value = "SELECT id, app, uri, COUNT(DISTINCT ip) hit, created FROM STAT " +
            "WHERE (created >= :createdStart AND created <= :createdEnd)",
            nativeQuery = true)
    List<ViewStatsDto> getByCreatedBetweenAndUniqueIp(
            @Param("createdStart") LocalDateTime createdStart,
            @Param("createdEnd") LocalDateTime createdEnd);
}
