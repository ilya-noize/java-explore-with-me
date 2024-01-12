package ru.practicum.compilation.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import prototype.Controller;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
public class CompilationController implements Controller<CompilationDto, NewCompilationDto> {
    @PostMapping({"/admin/compilations"})
    public CompilationDto create(@RequestBody @Valid NewCompilationDto newDto) {
        return new CompilationDto();
    }

    @PatchMapping({"/admin/compilations/{id}"})
    public CompilationDto update(@PathVariable Long id, @RequestBody @Valid NewCompilationDto newDto) {
        return new CompilationDto();
    }

    @GetMapping({"/compilations/{id}"})
    public CompilationDto get(@PathVariable Long id) {
        return null;
    }

    @GetMapping({"/compilations"})
    public List<CompilationDto> getAll() {
        return null;
    }

    @DeleteMapping({"/admin/compilations/{id}"})
    public void remove(@PathVariable Long id) {
    }
}
