package ru.practicum.user.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.user.api.dto.NewUserDto;
import ru.practicum.user.api.dto.UserDto;
import ru.practicum.user.entity.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    User toEntity(NewUserDto dto);
}
