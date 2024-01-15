package ru.practicum.compilation.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.category.api.dto.NewCategoryDto;
import ru.practicum.compilation.api.dto.CompilationDto;
import ru.practicum.compilation.entity.Compilation;

@Mapper
public interface CompilationMapper {
    CompilationMapper INSTANCE = Mappers.getMapper(CompilationMapper.class);

    CompilationDto toDto(Compilation compilation);

    @Mapping(target = "title", ignore = true)
    @Mapping(target = "pinned", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", ignore = true)
    Compilation toEntity(NewCategoryDto newCategoryDto);

    @Mapping(target = "events", ignore = true)
    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "pinned", source = "dto.pinned")
    @Mapping(target = "title", source = "dto.title")
    Compilation toEntity(CompilationDto dto);
}
