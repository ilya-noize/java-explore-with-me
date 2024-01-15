package ru.practicum.category.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.category.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    long removeById(Long id);

    //todo force delete with events (WARNING!)
}
