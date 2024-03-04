package ru.practicum.moderate.entity;

import org.apache.commons.lang3.EnumUtils;

public enum ReviewState {
    APPROVE, REJECT;

    public static boolean isValid(String eventState) {
        return EnumUtils.isValidEnum(
                ReviewState.class,
                eventState.toUpperCase()
        );
    }
}
