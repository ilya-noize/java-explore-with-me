package ru.practicum.event.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.event.api.dto.EventDto;
import ru.practicum.event.api.dto.EventShortDto;
import ru.practicum.event.api.dto.NewEventDto;
import ru.practicum.event.api.service.EventService;
import ru.practicum.event.entity.Event;
import ru.practicum.user.entity.User;

import java.time.LocalDateTime;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;

@Mapper(injectionStrategy = CONSTRUCTOR)
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "initiator", source = "initiator")
    @Mapping(target = "createdOn", source = "now")
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "confirmedRequests", ignore = true)
    @Mapping(target = "state", source = "state")
    @Mapping(target = "views", source = "0")
    Event toEntity(NewEventDto newEventDto,
                   User initiator,
                   LocalDateTime now,
                   EventService.EventState state);

    EventDto toDto(Event event);

    EventShortDto toShortDto(Event event);
}
