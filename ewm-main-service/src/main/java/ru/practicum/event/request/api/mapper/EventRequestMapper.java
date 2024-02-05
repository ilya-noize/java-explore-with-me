package ru.practicum.event.request.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.event.api.mapper.EventMapper;
import ru.practicum.event.request.api.dto.EventRequestDto;
import ru.practicum.event.request.entity.EventRequest;
import ru.practicum.user.api.mapper.UserMapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, EventMapper.class})
public interface EventRequestMapper {

    @Mapping(target = "event", source = "eventRequest.event.id")
    @Mapping(target = "requester", source = "eventRequest.requester.id")
    EventRequestDto toDto(EventRequest eventRequest);
}
