package ru.practicum.compilation.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.compilation.api.dto.CompilationDto;
import ru.practicum.compilation.entity.Compilation;
import ru.practicum.event.api.mapper.EventMapper;

@Mapper(uses = {EventMapper.class})
public interface CompilationMapper {
    CompilationMapper INSTANCE = Mappers.getMapper(CompilationMapper.class);

    CompilationDto toDto(Compilation compilation);
}
