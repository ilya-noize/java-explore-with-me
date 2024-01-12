package ru.practicum.user.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import prototype.Controller;
import ru.practicum.user.dto.NewUserDto;
import ru.practicum.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@RequestMapping({"/admin"})
@Validated
public class UserController implements Controller<UserDto, NewUserDto> {
    public UserController() {
    }

    @PostMapping({"/users"})
    public UserDto create(@RequestBody @Valid NewUserDto newDto) {
        return new UserDto();
    }

    public UserDto update(Long id, NewUserDto newDto) {
        return null;
    }

    public UserDto get(Long id) {
        return null;
    }

    @GetMapping({"/users"})
    public List<UserDto> getAll() {
        return List.of(new UserDto());
    }

    @DeleteMapping({"/users/{id}"})
    public void remove(@PathVariable Long id) {
    }
}
