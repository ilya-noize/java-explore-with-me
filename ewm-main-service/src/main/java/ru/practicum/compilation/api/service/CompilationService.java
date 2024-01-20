package ru.practicum.compilation.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.api.dto.CompilationDto;
import ru.practicum.compilation.api.dto.NewCompilationDto;
import ru.practicum.compilation.api.mapper.CompilationMapper;
import ru.practicum.compilation.api.repository.CompilationRepository;
import ru.practicum.compilation.entity.Compilation;
import ru.practicum.event.api.repository.EventRepository;
import ru.practicum.event.entity.Event;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;

import java.util.Collections;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static ru.practicum.constants.Constants.COMPILATION_NOT_EXISTS;
import static ru.practicum.constants.Constants.checkPageable;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationService {
    private final CompilationRepository repository;
    private final EventRepository eventRepository;

    public CompilationDto create(NewCompilationDto dto) {
        Compilation compilation = Compilation.builder()
                .title(dto.getTitle())
                .pinned(dto.getPinned() != null ? dto.getPinned() : false)
                .events(getEvents(dto.getEvents())).build();

        return CompilationMapper.INSTANCE.toDto(
                repository.save(compilation));
    }

    private List<Event> getEvents(List<Long> eventsIds) {
        log.debug("[i] get events in compilation IDs:{}", eventsIds);
        if (eventsIds != null) {
            return eventRepository.getByIdInOrderByIdAsc(eventsIds)
                    .orElseThrow(() ->
                            new BadRequestException("Non-existent events in the compilation"));
        } else {
            return Collections.emptyList();
        }
    }

    public CompilationDto update(Long id, CompilationDto dto) {
        Compilation compilation = getCompilation(id);

        String title = dto.getTitle();
        if (title != null) {
            log.debug("[i] title update");
            compilation.setTitle(title);
        }
        Boolean pinned = dto.getPinned();
        if (pinned != null) {
            log.debug("[i] pinned update");
            compilation.setPinned(pinned);
        }
        List<Long> eventIds = dto.getEvents();
        if (eventIds != null) {
            log.debug("[i] events update");
            compilation.setEvents(getEvents(eventIds));
        }

        return CompilationMapper.INSTANCE.toDto(
                repository.save(compilation));
    }

    private Compilation getCompilation(Long id) {
        log.debug("[i] get compilation ID:{}", id);

        return repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(format(COMPILATION_NOT_EXISTS, id)));
    }

    public CompilationDto get(Long id) {
        return CompilationMapper.INSTANCE.toDto(getCompilation(id));
    }

    public List<CompilationDto> getAll(Integer from, Integer size) {
        Pageable pageable = checkPageable(from, size, null);
        return repository.findAll(pageable).stream()
                .map(CompilationMapper.INSTANCE::toDto)
                .collect(toList());
    }

    public void remove(Long id) {
        if (repository.removeById(id) == 0) {
            throw new NotFoundException(format(COMPILATION_NOT_EXISTS, id));
        }
        log.debug("[i][admin] The compilation (ID:{}) was successfully deleted.", id);
    }
}
