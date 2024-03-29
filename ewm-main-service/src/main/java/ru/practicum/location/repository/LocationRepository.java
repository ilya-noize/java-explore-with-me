package ru.practicum.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.location.entity.Location;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> getByLatAndLon(double lat, double lon);
}
