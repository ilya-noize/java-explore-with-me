package ru.practicum.user.api.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import prototype.Constants;
import ru.practicum.user.api.dto.UserDto;
import ru.practicum.user.entity.User;

import java.util.Collection;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Transactional
    @Modifying
    @Query("update User u set u.userGroup = :ban where u.id = :id")
    void makeBannedUser(@Param("id") Long id,
                        @Param("ban") Constants.UserGroup ban);

    @Query("select u from User u where u.id in :ids and u.userGroup <> :userGroup order by u.id")
    List<UserDto> getByIdInAndUserGroupNotOrderByIdAsc(
            @Param("ids") Collection<Long> ids,
            @Param("userGroup") Constants.UserGroup userGroup,
            Pageable pageable);
}
