package ru.practicum.event.request.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.event.request.api.dto.EventRequestDto;
import ru.practicum.event.request.api.dto.NewEventRequestDto;
import ru.practicum.event.request.entity.EventRequest;

@Mapper
public interface EventRequestMapper {
    EventRequestMapper INSTANCE = Mappers.getMapper(EventRequestMapper.class);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "requester", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "event", ignore = true)
    EventRequest toEntity(NewEventRequestDto newEventRequestDto);

    EventRequestDto toDto(EventRequest eventRequest);
}
