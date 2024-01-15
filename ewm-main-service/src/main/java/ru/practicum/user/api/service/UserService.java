package ru.practicum.user.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.exception.BadRequestException;
import ru.practicum.user.api.dto.NewUserDto;
import ru.practicum.user.api.dto.UserDto;
import ru.practicum.user.api.mapper.UserMapper;
import ru.practicum.user.api.repository.UserRepository;
import ru.practicum.user.entity.User;

import java.util.List;

import static prototype.Constants.USER_NOT_EXISTS;
import static prototype.Constants.UserGroup.BAN;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    public UserDto create(NewUserDto newDto) {
        log.debug("[i] create user");
        User user = mapper.toEntity(newDto);

        return mapper.toDto(repository.save(user));
    }

    public List<UserDto> getAll(List<Long> ids, Pageable pageable) {
        log.debug("[i] get users");

        return repository.getByIdInAndUserGroupNotOrderByIdAsc(ids, BAN, pageable);
    }

    public void remove(Long id) {
        log.debug("[i] make banned user");
        isExistUserById(id);
        repository.makeBannedUser(id, BAN);
    }

    private void isExistUserById(long id) {
        if (!repository.existsById(id)) {
            throw new BadRequestException(
                    String.format(USER_NOT_EXISTS, id));
        }
    }
}
