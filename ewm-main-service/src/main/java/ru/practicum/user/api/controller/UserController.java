package ru.practicum.user.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.user.api.dto.NewUserDto;
import ru.practicum.user.api.dto.UserDto;
import ru.practicum.user.api.service.UserService;

import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.constants.Constants.FROM;
import static ru.practicum.constants.Constants.SIZE;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping({"/admin/users"})
    public UserDto create(@RequestBody @Validated NewUserDto dto) {
        log.debug("[i] create new user. dto: {}", dto);

        return service.create(dto);
    }

    @GetMapping({"/admin/users"})
    public List<UserDto> getAll(
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(required = false, defaultValue = FROM) @Min(0) Integer from,
            @RequestParam(required = false, defaultValue = SIZE) @Min(1) Integer size) {
        log.debug("[i] get all users with ids : {}. from:{}, size:{}", ids, from, size);

        return service.getAll(ids, from, size);
    }

    @DeleteMapping({"/admin/users/{id}"})
    public void remove(@PathVariable Long id) {
        log.debug("[i] remove user with id: {}", id);

        service.remove(id);
    }
}
