package ru.practicum.compilation.api.controller;

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
import ru.practicum.compilation.api.dto.CompilationDto;
import ru.practicum.compilation.api.dto.UpdateCompilationDto;
import ru.practicum.compilation.api.service.CompilationService;
import ru.practicum.compilation.api.dto.NewCompilationDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.constants.Constants.FROM;
import static ru.practicum.constants.Constants.SIZE;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class CompilationController {
    private final CompilationService service;

    @PostMapping({"/admin/compilations"})
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@RequestBody @Valid NewCompilationDto dto) {
        log.debug("[i][admin] create new compilation");

        return service.create(dto);
    }

    @PatchMapping({"/admin/compilations/{id}"})
    public CompilationDto update(@PathVariable Long id, @RequestBody @Valid UpdateCompilationDto dto) {
        log.debug("[i][admin] update compilation ID:{}", id);

        return service.update(id, dto);
    }

    @GetMapping({"/compilations/{id}"})
    public CompilationDto get(@PathVariable Long id) {
        log.debug("[i][] get compilation ID:{}", id);

        return service.get(id);
    }

    @GetMapping({"/compilations"})
    public List<CompilationDto> getAll(@RequestParam(required = false, defaultValue = FROM) @Min(0) Integer from,
                                       @RequestParam(required = false, defaultValue = SIZE) @Min(1) Integer size) {
        log.debug("[i][] get all compilations");

        return service.getAll(from, size);
    }

    @DeleteMapping({"/admin/compilations/{compId}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long compId) {
        log.debug("[i][admin] remove compilation ID:{}", compId);
        service.remove(compId);
    }
}
