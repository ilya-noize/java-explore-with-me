package ru.practicum.user.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.api.dto.NewUserDto;
import ru.practicum.user.api.dto.UserDto;
import ru.practicum.user.api.mapper.UserMapper;
import ru.practicum.user.api.repository.UserRepository;
import ru.practicum.user.entity.User;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.practicum.constants.Constants.USER_NOT_EXISTS;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDto create(NewUserDto newDto) {
        log.debug("[i] create user");
        User user = UserMapper.INSTANCE.toEntity(newDto);

        return UserMapper.INSTANCE.toDto(repository.save(user));
    }

    @Override
    public List<UserDto> getAll(List<Long> ids, Integer from, Integer size) {
        log.debug("[i] get users");

        List<User> allUsersByIds = repository.findAllById(ids);
        return allUsersByIds.stream()
                .map(UserMapper.INSTANCE::toDto)
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
