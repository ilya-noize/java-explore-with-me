package ru.practicum.compilation.entity;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.event.entity.Event;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Getter
@Setter
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean pinned;
    private String title;
    @ManyToMany
    private List<Event> events;
}
