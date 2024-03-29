package ru.practicum.category.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.category.api.dto.CategoryDto;
import ru.practicum.category.api.dto.NewCategoryDto;
import ru.practicum.category.api.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.constants.Constants.FROM;
import static ru.practicum.constants.Constants.SIZE;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class CategoryController {
    private final CategoryService service;

    @PostMapping({"/admin/categories"})
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@RequestBody @Valid NewCategoryDto dto) {
        log.debug("[i][admin] new category: {}", dto.getName());

        return service.create(dto);
    }

    @PatchMapping({"/admin/categories/{id}"})
    public CategoryDto update(@PathVariable Long id,
                              @RequestBody @Valid NewCategoryDto dto) {
        log.debug("[i][admin] update category: ID:{} -> {}", id, dto.getName());

        return service.update(id, dto);
    }

    @GetMapping({"/categories/{id}"})
    public CategoryDto get(@PathVariable Long id) {
        log.debug("[i] get category ID:{}", id);

        return service.get(id);
    }

    @GetMapping({"/categories"})
    public List<CategoryDto> getAll(@RequestParam(required = false, defaultValue = FROM) @Min(0) Integer from,
                                    @RequestParam(required = false, defaultValue = SIZE) @Min(1) Integer size) {
        log.debug("[i] get list categories");

        return service.getAll(from, size);
    }

    @DeleteMapping({"/admin/categories/{id}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long id) {
        log.debug("[i][admin] remove category ID:{}", id);
        service.remove(id);
    }
}