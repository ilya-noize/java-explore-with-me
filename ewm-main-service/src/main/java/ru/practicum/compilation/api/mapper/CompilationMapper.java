package ru.practicum.compilation.api.mapper;

import org.mapstruct.Mapper;
import ru.practicum.compilation.api.dto.CompilationDto;
import ru.practicum.compilation.entity.Compilation;
import ru.practicum.event.api.mapper.EventMapper;

@Mapper(componentModel = "spring", uses = {EventMapper.class})
public interface CompilationMapper {

    CompilationDto toDto(Compilation compilation);
}
