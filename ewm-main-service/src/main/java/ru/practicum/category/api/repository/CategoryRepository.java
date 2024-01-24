package ru.practicum.category.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.category.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByNameIgnoreCase(String name);

}
