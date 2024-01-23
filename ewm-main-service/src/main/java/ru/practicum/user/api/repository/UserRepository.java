package ru.practicum.user.api.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
