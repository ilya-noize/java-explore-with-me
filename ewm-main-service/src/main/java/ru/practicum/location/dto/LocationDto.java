package ru.practicum.location.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <h3>Местоположение</h3>
 * #{@link #lat} Широта <br/>
 * #{@link #lon} Долгота <br/>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class LocationDto {
    @NotNull
    private Double lat;
    @NotNull
    private Double lon;
}
