package ru.practicum.category.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import static ru.practicum.constants.Constants.MAX_CATEGORY_NAME_LENGTH;

/**
 * <h3>Категории</h3>
 * {@link #id} ID<br/>
 * {@link #name} Имя категории<br/>
 */
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    @Column(
            length = MAX_CATEGORY_NAME_LENGTH,
            nullable = false,
            unique = true
    )
    private String name;
}