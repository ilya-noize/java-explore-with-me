package ru.practicum.event.request.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateResponse {
    private EventRequestDto confirmedRequests;
    private EventRequestDto rejectedRequests;
}
