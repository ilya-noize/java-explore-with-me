package ru.practicum.category.api.service;

import ru.practicum.category.api.dto.CategoryDto;
import ru.practicum.category.api.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    /**
     * <h4>Создаёт новую категорию</h4>
     * @param newDto Новая категория <br/>
     * @return Сохранённая категория с новым ID <br/>
     */
    CategoryDto create(NewCategoryDto newDto);

    /**
     * <h4>Переименовать категорию</h4>
     * @param id ID категории <br/>
     * @param newDto Переименованная категория <br/>
     * @return Изменённая категория <br/>
     */
    CategoryDto update(Long id, NewCategoryDto newDto);

    /**
     * <h4>Получает категорию</h4>
     * @param id ID категории <br/>
     * @return Категория <br/>
     */
    CategoryDto get(Long id);

    /**
     * <h4>Получает список категорий</h4>
     * @param from Параметр постраничного списка (начало списка) <br/>
     * @param size Параметр постраничного списка (количество записей за раз) <br/>
     * @return Список категорий <br/>
     */
    List<CategoryDto> getAll(Integer from, Integer size);

    /**
     * <h4>Удаляет категорию</h4>
     * @param id ID категории <br/>
     */
    void remove(Long id);
}
