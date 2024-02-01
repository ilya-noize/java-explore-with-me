package ru.practicum.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.ViewStats;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Stat, Long> {
    /**
     * @param createdStart Дата и время начала диапазона за который нужно выгрузить статистику
     * @param createdEnd   Дата и время конца диапазона за который нужно выгрузить статистику
     * @param uris         Список url для которых нужно выгрузить статистику
     * @param sort          Сортировка
     * @return Список всех url с количеством запросов
     */
    @Query(value = "SELECT new ru.practicum.model.ViewStats (s.app, s.uri, COUNT(s.ip) AS hits) " +
            " FROM Stat AS s " +
            " WHERE s.uri IN (:uris) " +
            " AND created BETWEEN :createdStart AND :createdEnd " +
            " GROUP BY s.app, s.uri " +
            " ORDER BY hits DESC")
    List<ViewStats> getByCreatedBetweenAndUriIn(
            @Param("createdStart") LocalDateTime createdStart,
            @Param("createdEnd") LocalDateTime createdEnd,
            @Param("uris") String[] uris, Sort sort);

    /**
     * @param createdStart Дата и время начала диапазона за который нужно выгрузить статистику
     * @param createdEnd   Дата и время конца диапазона за который нужно выгрузить статистику
     * @param sort          Сортировка
     * @return Список всех запросов
     */
    @Query(value = "SELECT new ru.practicum.model.ViewStats (s.app, s.uri, COUNT(s.ip) AS hits) " +
            " FROM Stat AS s " +
            " WHERE created BETWEEN :createdStart AND :createdEnd " +
            " GROUP BY s.app, s.uri " +
            " ORDER BY hits DESC")
    List<ViewStats> getByCreatedBetween(
            @Param("createdStart") LocalDateTime createdStart,
            @Param("createdEnd") LocalDateTime createdEnd, Sort sort);

    /**
     * @param createdStart Дата и время начала диапазона за который нужно выгрузить статистику
     * @param createdEnd   Дата и время конца диапазона за который нужно выгрузить статистику
     * @param uris         Список url для которых нужно выгрузить статистику
     * @param sort          Сортировка
     * @return Список всех url с количеством уникальных запросов
     */
    @Query(value = "SELECT DISTINCT new ru.practicum.model.ViewStats (s.app, s.uri, COUNT(DISTINCT s.ip) AS hits)" +
            " FROM Stat AS s " +
            " WHERE s.uri IN (:uris) " +
            " AND created BETWEEN :createdStart AND :createdEnd " +
            " GROUP BY s.app, s.uri " +
            " ORDER BY hits DESC")
    List<ViewStats> getByCreatedBetweenAndUriInAndUniqueIp(
            @Param("createdStart") LocalDateTime createdStart,
            @Param("createdEnd") LocalDateTime createdEnd,
            @Param("uris") String[] uris, Sort sort);

    /**
     * @param createdStart Дата и время начала диапазона за который нужно выгрузить статистику
     * @param createdEnd   Дата и время конца диапазона за который нужно выгрузить статистику
     * @param sort          Сортировка
     * @return Список всех уникальных запросов
     */
    @Query(value = "SELECT DISTINCT new ru.practicum.model.ViewStats (s.app, s.uri, COUNT(DISTINCT s.ip) AS hits)" +
            " FROM Stat AS s " +
            " WHERE created BETWEEN :createdStart AND :createdEnd " +
            " GROUP BY s.app, s.uri " +
            " ORDER BY hits DESC")
    List<ViewStats> getByCreatedBetweenAndUniqueIp(
            @Param("createdStart") LocalDateTime createdStart,
            @Param("createdEnd") LocalDateTime createdEnd, Sort sort);
}
