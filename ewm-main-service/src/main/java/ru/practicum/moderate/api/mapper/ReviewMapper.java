package ru.practicum.moderate.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.event.api.mapper.EventMapper;
import ru.practicum.moderate.api.dto.NewReviewDto;
import ru.practicum.moderate.api.dto.ReviewDto;
import ru.practicum.moderate.entity.Review;

@Mapper(componentModel = "spring",
        uses = {EventMapper.class}
)
public interface ReviewMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "event.id", source = "dto.eventId")
    Review toEntity(NewReviewDto dto);

    ReviewDto toDto(Review entity);
}
