package ru.practicum.event.request.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.event.request.api.dto.NewEventRequestDto;
import ru.practicum.event.request.entity.EventRequest;

@Mapper
public interface EventRequestMapper {
    EventRequestMapper INSTANCE = Mappers.getMapper(EventRequestMapper.class);

    EventRequest toEntity(NewEventRequestDto newEventRequestDto);
}
