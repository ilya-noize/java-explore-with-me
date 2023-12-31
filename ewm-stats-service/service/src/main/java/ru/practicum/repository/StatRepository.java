package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.ShowStats;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Stats, Long> {
    /**
     * @param createdStart  Дата и время начала диапазона за который нужно выгрузить статистику
     * @param createdEnd    Дата и время конца диапазона за который нужно выгрузить статистику
     * @param uris          Список url для которых нужно выгрузить статистику
     * @return      Список всех url с количеством запросов
     */
    @Query(value = "SELECT s.APP app, s.URI uri, COUNT(s.IP) hits" +
            " FROM STAT s " +
            " WHERE (s.CREATED >= :createdStart AND s.CREATED <= :createdEnd)" +
            " AND s.uri IN (:uris) " +
            " GROUP BY s.APP, s.URI " +
            " ORDER BY hits",
            nativeQuery = true)
    List<ShowStats> getByCreatedBetweenAndUriIn(
            @Param("createdStart") LocalDateTime createdStart,
            @Param("createdEnd") LocalDateTime createdEnd,
            @Param("uris") String uris);

    /**
     * @param createdStart  Дата и время начала диапазона за который нужно выгрузить статистику
     * @param createdEnd    Дата и время конца диапазона за который нужно выгрузить статистику
     * @return      Список всех запросов
     */
    @Query(value = "SELECT s.APP app, s.URI uri, COUNT(s.IP) hits" +
            " FROM STAT as s " +
            " WHERE (created >= :createdStart AND created <= :createdEnd) " +
            " GROUP BY s.APP, s.URI " +
            " ORDER BY hits",
            nativeQuery = true)
    List<ShowStats> getByCreatedBetween(
            @Param("createdStart") LocalDateTime createdStart,
            @Param("createdEnd") LocalDateTime createdEnd);

    /**
     * @param createdStart  Дата и время начала диапазона за который нужно выгрузить статистику
     * @param createdEnd    Дата и время конца диапазона за который нужно выгрузить статистику
     * @param uris          Список url для которых нужно выгрузить статистику
     * @return      Список всех url с количеством уникальных запросов
     */
    @Query(value = "SELECT s.APP app, s.URI uri, COUNT(DISTINCT s.IP) hits" +
            " FROM STAT as s " +
            " WHERE (created >= :createdStart AND created <= :createdEnd)" +
            " AND uri IN (:uris) " +
            " GROUP BY s.APP, s.URI " +
            " ORDER BY hits",
            nativeQuery = true)
    List<ShowStats> getByCreatedBetweenAndUriInAndUniqueIp(
            @Param("createdStart") LocalDateTime createdStart,
            @Param("createdEnd") LocalDateTime createdEnd,
            @Param("uris") String uris);

    /**
     * @param createdStart  Дата и время начала диапазона за который нужно выгрузить статистику
     * @param createdEnd    Дата и время конца диапазона за который нужно выгрузить статистику
     * @return      Список всех уникальных запросов
     */
    @Query(value = "SELECT s.APP app, s.URI uri, COUNT(DISTINCT s.IP) hits" +
            " FROM STAT as s " +
            " WHERE (created >= :createdStart AND created <= :createdEnd) " +
            " GROUP BY s.APP, s.URI " +
            " ORDER BY hits",
            nativeQuery = true)
    List<ShowStats> getByCreatedBetweenAndUniqueIp(
            @Param("createdStart") LocalDateTime createdStart,
            @Param("createdEnd") LocalDateTime createdEnd);
}
