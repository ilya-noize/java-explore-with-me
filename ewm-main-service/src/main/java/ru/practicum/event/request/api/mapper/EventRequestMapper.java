package ru.practicum.event.request.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.event.request.api.dto.EventRequestDto;
import ru.practicum.event.request.entity.EventRequest;

@Mapper
public interface EventRequestMapper {
    EventRequestMapper INSTANCE = Mappers.getMapper(EventRequestMapper.class);

    EventRequestDto toDto(EventRequest eventRequest);
}
