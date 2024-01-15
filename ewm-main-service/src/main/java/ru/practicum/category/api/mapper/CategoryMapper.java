package ru.practicum.category.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.category.api.dto.CategoryDto;
import ru.practicum.category.api.dto.NewCategoryDto;
import ru.practicum.category.entity.Category;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;

@Mapper(injectionStrategy = CONSTRUCTOR)
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDto toDto(Category category);

    @Mapping(target = "id", ignore = true)
    Category toEntity(NewCategoryDto newCategoryDto);
}
