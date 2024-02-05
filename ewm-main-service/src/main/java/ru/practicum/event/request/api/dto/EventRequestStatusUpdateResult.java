package ru.practicum.event.request.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
    private final List<EventRequestDto> confirmedRequests = new ArrayList<>();
    private final List<EventRequestDto> rejectedRequests = new ArrayList<>();
}
