package ru.practicum.category.api.service;

import ru.practicum.category.api.dto.CategoryDto;
import ru.practicum.category.api.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto create(NewCategoryDto newDto);

    CategoryDto update(Long id, NewCategoryDto newDto);

    CategoryDto get(Long id);

    List<CategoryDto> getAll(Integer from, Integer size);

    void remove(Long id);
}
