package ru.practicum.user.api.service;

import ru.practicum.user.api.dto.NewUserDto;
import ru.practicum.user.api.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(NewUserDto newDto);

    List<UserDto> getAll(List<Long> ids, Integer from, Integer size);

    void remove(long id);
}
