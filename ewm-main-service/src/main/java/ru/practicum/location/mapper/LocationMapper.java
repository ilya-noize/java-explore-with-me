package ru.practicum.location.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.entity.Location;

@Mapper
public interface LocationMapper {
    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    Location toEntity(LocationDto dto);

    LocationDto toDto(Location location);
}
