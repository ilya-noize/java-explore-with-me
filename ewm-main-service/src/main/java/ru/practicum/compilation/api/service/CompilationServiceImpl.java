package ru.practicum.compilation.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.api.dto.CompilationDto;
import ru.practicum.compilation.api.dto.NewCompilationDto;
import ru.practicum.compilation.api.dto.UpdateCompilationDto;
import ru.practicum.compilation.api.mapper.CompilationMapper;
import ru.practicum.compilation.api.repository.CompilationRepository;
import ru.practicum.compilation.entity.Compilation;
import ru.practicum.event.api.repository.EventRepository;
import ru.practicum.event.entity.Event;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
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
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto create(NewCompilationDto dto) {
        String title = dto.getTitle();
        Compilation compilation = Compilation.builder()
                .title(isExistsTitle(title))
                .pinned(dto.getPinned() != null ? dto.getPinned() : false)
                .events(getEvents(dto.getEvents())).build();

        return CompilationMapper.INSTANCE.toDto(
                compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto update(Long id, UpdateCompilationDto dto) {
        Compilation compilation = getCompilation(id);

        String title = dto.getTitle();
        if (title != null) {
            compilation.setTitle(isExistsTitle(title));
            log.debug("[i] title updated");
        }
        Boolean pinned = dto.getPinned();
        if (pinned != null) {
            log.debug("[i] pinned updated");
            compilation.setPinned(pinned);
        }
        List<Long> eventIds = dto.getEvents();
        if (eventIds != null) {
            log.debug("[i] events updated");
            compilation.setEvents(getEvents(eventIds));
        }

        return CompilationMapper.INSTANCE.toDto(
                compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto get(Long id) {
        return CompilationMapper.INSTANCE.toDto(getCompilation(id));
    }

    @Override
    public List<CompilationDto> getAll(Integer from, Integer size) {
        Pageable pageable = checkPageable(from, size, null);
        return compilationRepository.findAll(pageable).stream()
                .map(CompilationMapper.INSTANCE::toDto)
                .collect(toList());
    }

    @Override
    public void remove(Long id) {
        if (compilationRepository.removeById(id) == 0) {
            throw new NotFoundException(format(COMPILATION_NOT_EXISTS, id));
        }
        log.debug("[i][admin] The compilation (ID:{}) was successfully deleted.", id);
    }

    private String isExistsTitle(String title) {
        boolean isExistsTitle = compilationRepository.existsByTitleIgnoreCase(title);
        if (isExistsTitle) throw new ConflictException("Compilation with this title exists.");
        return title;
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

    private Compilation getCompilation(Long id) {
        log.debug("[i] get compilation ID:{}", id);

        return compilationRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(format(COMPILATION_NOT_EXISTS, id)));
    }
}
