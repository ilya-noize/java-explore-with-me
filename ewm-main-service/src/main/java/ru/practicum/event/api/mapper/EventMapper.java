package ru.practicum.event.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.category.api.mapper.CategoryMapper;
import ru.practicum.event.api.dto.EventDto;
import ru.practicum.event.api.dto.EventShortDto;
import ru.practicum.event.api.dto.NewEventDto;
import ru.practicum.event.entity.Event;
import ru.practicum.event.request.api.mapper.EventRequestMapper;
import ru.practicum.location.mapper.LocationMapper;
import ru.practicum.moderate.api.mapper.ReviewMapper;
import ru.practicum.user.api.mapper.UserMapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = {
        UserMapper.class,
        EventRequestMapper.class,
        LocationMapper.class,
        CategoryMapper.class,
        ReviewMapper.class
})
public interface EventMapper {

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "views", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "confirmedRequests", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    Event toEntity(NewEventDto newEventDto);

    EventDto toDto(Event event);

    EventShortDto toShortDto(Event event);
}
