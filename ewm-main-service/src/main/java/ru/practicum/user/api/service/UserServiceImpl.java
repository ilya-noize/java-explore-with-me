package ru.practicum.user.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.api.dto.NewUserDto;
import ru.practicum.user.api.dto.UserDto;
import ru.practicum.user.api.mapper.UserMapper;
import ru.practicum.user.api.repository.UserRepository;
import ru.practicum.user.entity.User;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.practicum.constants.Constants.USER_NOT_EXISTS;
import static ru.practicum.constants.Constants.checkPageable;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public UserDto create(NewUserDto dto) {
        log.debug("[i] create user");
        User user = User.builder()
                .name(isExistsName(dto.getName()))
                .email(isExistsEmail(dto.getEmail())).build();

        return mapper.toDto(repository.save(user));
    }

    private String isExistsEmail(String email) {
        boolean isExists = repository.existsByEmailIgnoreCase(email);
        if (isExists) throw new ConflictException("User with this email exists.");
        return email;
    }

    private String isExistsName(String name) {
        boolean isExists = repository.existsByNameIgnoreCase(name);
        if (isExists) throw new ConflictException("User with this name exists.");
        return name;
    }

    @Override
    public List<UserDto> getAll(List<Long> ids, Integer from, Integer size) {
        log.debug("[i] get users from");
        List<User> allUsersByIds;
        if (ids == null) {
            allUsersByIds = repository.findAll(checkPageable(from, size, null)).toList();
        } else {
            allUsersByIds = repository.findAllById(ids);
        }

        return allUsersByIds.stream()
                .map(mapper::toDto)
                .collect(toList());
    }


    @Override
    public void remove(long id) {
        log.debug("[i] delete user ID:{}", id);
        try {
            repository.deleteById(id);
        } catch (RuntimeException e) {
            throw new NotFoundException(String.format(USER_NOT_EXISTS, id));
        }
    }
}
