package ru.practicum.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import static ru.practicum.constants.Constants.MAX_USER_EMAIL_LENGTH;
import static ru.practicum.constants.Constants.MAX_USER_NAME_LENGTH;

/**
 *
 * <p>Пользователи</p>
 * #{@link #id} ID <br/>
 * #{@link #name} Name<br/>
 * #{@link #email} E-Mail<br/>
 */
@Entity()
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    private @Column(unique = true, length = MAX_USER_EMAIL_LENGTH) String email;
    private @Column(length = MAX_USER_NAME_LENGTH) String name;
}
