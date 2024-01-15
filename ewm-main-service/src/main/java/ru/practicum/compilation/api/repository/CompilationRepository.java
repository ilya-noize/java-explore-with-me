package ru.practicum.compilation.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.compilation.entity.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    long removeById(Long id);
}
