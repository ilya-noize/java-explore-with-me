package ru.practicum.event.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.event.api.dto.EventDto;
import ru.practicum.event.api.dto.NewEventDto;
import ru.practicum.event.entity.Event;
import ru.practicum.user.entity.User;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;

@Mapper(injectionStrategy = CONSTRUCTOR)
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "id",                 ignore = true)
    @Mapping(target = "createdOn",          ignore = true)
    @Mapping(target = "publishedOn",        ignore = true)
    @Mapping(target = "state",              ignore = true)
    @Mapping(target = "confirmedRequests",  ignore = true)
    @Mapping(target = "views",              ignore = true)
    @Mapping(target = "initiator", source = "initiator")
    Event toEntity(NewEventDto newEventDto, User initiator);

    EventDto toDto(Event event);
}
