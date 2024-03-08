package ru.practicum.compilation.api.mapper;

import org.mapstruct.Mapper;
import ru.practicum.compilation.api.dto.CompilationDto;
import ru.practicum.compilation.entity.Compilation;
import ru.practicum.event.api.mapper.EventMapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = {EventMapper.class})
public interface CompilationMapper {

    CompilationDto toDto(Compilation compilation);
}
