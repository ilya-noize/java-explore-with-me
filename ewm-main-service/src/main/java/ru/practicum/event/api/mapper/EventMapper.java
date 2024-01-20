package ru.practicum.event.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.category.api.mapper.CategoryMapper;
import ru.practicum.event.api.dto.EventDto;
import ru.practicum.event.api.dto.EventShortDto;
import ru.practicum.event.api.dto.NewEventDto;
import ru.practicum.event.entity.Event;
import ru.practicum.event.request.api.mapper.EventRequestMapper;
import ru.practicum.location.mapper.LocationMapper;
import ru.practicum.user.api.mapper.UserMapper;

@Mapper(uses = {
        UserMapper.class,
        EventRequestMapper.class,
        LocationMapper.class,
        CategoryMapper.class
})
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "views", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "confirmedRequests", ignore = true)
    Event toEntity(NewEventDto newEventDto);

    @Mapping(target = "confirmedRequests", source = "confirmedRequests")
    EventDto toDto(Event event, int confirmedRequests);

    @Mapping(target = "confirmedRequests", ignore = true)
    EventShortDto toShortDto(Event event);
}
