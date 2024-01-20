package ru.practicum.category.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.api.dto.CategoryDto;
import ru.practicum.category.api.dto.NewCategoryDto;
import ru.practicum.category.api.mapper.CategoryMapper;
import ru.practicum.category.api.repository.CategoryRepository;
import ru.practicum.category.entity.Category;
import ru.practicum.event.api.repository.EventRepository;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.NotFoundException;

import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static ru.practicum.constants.Constants.CATEGORY_NOT_EXISTS;
import static ru.practicum.constants.Constants.checkPageable;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;
    private EventRepository eventRepository;

    @Override
    public CategoryDto create(NewCategoryDto newDto) {
        Category category = CategoryMapper.INSTANCE.toEntity(newDto);

        return CategoryMapper.INSTANCE.toDto(
                categoryRepository.save(category));
    }

    @Override
    public CategoryDto update(Long id, NewCategoryDto newDto) {
        Category category = getCategory(id);
        category.setName(newDto.getName());

        return CategoryMapper.INSTANCE.toDto(
                categoryRepository.save(category));
    }

    @Override
    public CategoryDto get(Long id) {

        return CategoryMapper.INSTANCE.toDto(getCategory(id));
    }

    private Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(format(CATEGORY_NOT_EXISTS, id)));
    }

    @Override
    public List<CategoryDto> getAll(Integer from, Integer size) {
        Pageable pageable = checkPageable(from, size, null);
        return categoryRepository.findAll(pageable)
                .stream()
                .map(CategoryMapper.INSTANCE::toDto)
                .collect(toList());
    }

    @Override
    public void remove(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException(format(CATEGORY_NOT_EXISTS, id));
        }
        if (eventRepository.countByCategory_Id(id) > 0) {
            throw new ForbiddenException("There are events in the category.");
        }
        categoryRepository.deleteById(id);
        log.debug("[i][admin] The category (ID:{}) was successfully deleted.", id);
    }
}
