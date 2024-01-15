package ru.practicum.user.api.mapper;

import ru.practicum.user.api.dto.NewUserDto;
import ru.practicum.user.api.dto.UserDto;
import ru.practicum.user.entity.User;

public class UserMapper {
    public UserDto toDto(User user) {

        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public User toEntity(NewUserDto dto) {

        return User.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .build();
    }
}
