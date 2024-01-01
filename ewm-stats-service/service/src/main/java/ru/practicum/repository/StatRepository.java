package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.ViewStats;
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
    @Query(value = "SELECT new ru.practicum.model.ViewStats (s.app, s.uri, COUNT(s.ip) AS hits) " +
            " FROM Stat AS s " +
            " WHERE s.uri IN (:uris) " +
            " AND created BETWEEN :createdStart AND :createdEnd " +
            " GROUP BY s.app, s.uri " +
            " ORDER BY hits")
    List<ViewStats> getByCreatedBetweenAndUriIn(
            @Param("createdStart") LocalDateTime createdStart,
            @Param("createdEnd") LocalDateTime createdEnd,
            @Param("uris") String[] uris);

//    @Query("select new ru.practicum.model.ViewStats (s.app, s.uri, count(s.ip) as hits) from Stat s " +
//            "where s.uri in (?1) and s.timestamp between ?2 and ?3 group by s.ip, s.uri, s.app order by hits desc")
//    List<ViewStats> getByUris(String[] uris, LocalDateTime start, LocalDateTime end);

    /**
     * @param createdStart  Дата и время начала диапазона за который нужно выгрузить статистику
     * @param createdEnd    Дата и время конца диапазона за который нужно выгрузить статистику
     * @return      Список всех запросов
     */
    @Query(value = "SELECT new ru.practicum.model.ViewStats (s.app, s.uri, COUNT(s.ip) AS hits) " +
            " FROM Stat AS s " +
            " WHERE created BETWEEN :createdStart AND :createdEnd " +
            " GROUP BY s.app, s.uri " +
            " ORDER BY hits")
    List<ViewStats> getByCreatedBetween(
            @Param("createdStart") LocalDateTime createdStart,
            @Param("createdEnd") LocalDateTime createdEnd);

//    @Query("select new ru.practicum.model.ViewStats (s.app, s.uri, count(s.ip) as hits) from Stat s " +
//            "where s.timestamp between ?1 and ?2 group by s.ip, s.uri, s.app order by hits desc")
//    List<ViewStats> getByStartAndEnd(LocalDateTime start, LocalDateTime end);

    /**
     * @param createdStart  Дата и время начала диапазона за который нужно выгрузить статистику
     * @param createdEnd    Дата и время конца диапазона за который нужно выгрузить статистику
     * @param uris          Список url для которых нужно выгрузить статистику
     * @return      Список всех url с количеством уникальных запросов
     */
    @Query(value = "SELECT DISTINCT new ru.practicum.model.ViewStats (s.app, s.uri, COUNT(DISTINCT s.ip) AS hits)" +
            " FROM Stat AS s " +
            " WHERE s.uri IN (:uris) " +
            " AND created BETWEEN :createdStart AND :createdEnd " +
            " GROUP BY s.app, s.uri " +
            " ORDER BY hits")
    List<ViewStats> getByCreatedBetweenAndUriInAndUniqueIp(
            @Param("createdStart") LocalDateTime createdStart,
            @Param("createdEnd") LocalDateTime createdEnd,
            @Param("uris") String[] uris);

//    @Query("select distinct new ru.practicum.model.ViewStats (s.app, s.uri, count(distinct s.ip) as hits) " +
//            "from Stat s where s.uri in (?1) and s.timestamp between ?2 and ?3 group by s.ip, s.uri, s.app " +
//            "order by hits desc")
//    List<ViewStats> getDistinctByUris(String[] uris, LocalDateTime start, LocalDateTime end);

    /**
     * @param createdStart  Дата и время начала диапазона за который нужно выгрузить статистику
     * @param createdEnd    Дата и время конца диапазона за который нужно выгрузить статистику
     * @return      Список всех уникальных запросов
     */
    @Query(value = "SELECT DISTINCT new ru.practicum.model.ViewStats (s.app, s.uri, COUNT(DISTINCT s.ip) AS hits)" +
            " FROM Stat AS s " +
            " WHERE created BETWEEN :createdStart AND :createdEnd " +
            " GROUP BY s.app, s.uri " +
            " ORDER BY hits")
    List<ViewStats> getByCreatedBetweenAndUniqueIp(
            @Param("createdStart") LocalDateTime createdStart,
            @Param("createdEnd") LocalDateTime createdEnd);

//    @Query("select distinct new ru.practicum.model.ViewStats (s.app, s.uri, count(distinct s.ip) as hits) " +
//            "from Stat s where s.timestamp between ?1 and ?2 group by s.ip, s.uri, s.app order by hits desc")
//    List<ViewStats> getDistinctByStartAndEnd(LocalDateTime start, LocalDateTime end);
}
