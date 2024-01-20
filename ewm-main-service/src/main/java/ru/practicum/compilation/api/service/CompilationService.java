package ru.practicum.compilation.api.service;

import ru.practicum.compilation.api.dto.CompilationDto;
import ru.practicum.compilation.api.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto create(NewCompilationDto dto);

    CompilationDto update(Long id, CompilationDto dto);

    CompilationDto get(Long id);

    List<CompilationDto> getAll(Integer from, Integer size);

    void remove(Long id);
}
