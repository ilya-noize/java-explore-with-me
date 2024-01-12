package ru.practicum.category.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import prototype.Controller;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

@RestController
@Validated
public class CategoriesController implements Controller<CategoryDto, NewCategoryDto> {
    public CategoriesController() {
    }

    @PostMapping({"/admin/categories"})
    public CategoryDto create(@RequestBody @Valid NewCategoryDto newDto) {
        return new CategoryDto();
    }

    @PatchMapping({"/admin/categories/{id}"})
    public CategoryDto update(@PathVariable Long id, @RequestBody @Valid NewCategoryDto newDto) {
        return new CategoryDto();
    }

    @GetMapping({"/categories/{id}"})
    public CategoryDto get(@PathVariable Long id) {
        return null;
    }

    @GetMapping({"/categories"})
    public List<CategoryDto> getAll() {
        return null;
    }

    @DeleteMapping({"/admin/categories/{id}"})
    public void remove(@PathVariable Long id) {
    }
}