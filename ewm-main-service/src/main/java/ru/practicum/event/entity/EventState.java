package ru.practicum.event.entity;

import org.apache.commons.lang3.EnumUtils;

/**
 * <h3>Состояние события</h3>
 * {@link #PENDING} - на модерации, <br/>
 * {@link #PUBLISHED} - подтверждена модератором, <br/>
 * {@link #CANCELED} - отклонена инициатором, <br/>
 * <p>
 * {@link #isValid(String)} - проверка строки на соответствие набору статусов <br/>
 */
public enum EventState {
    PENDING,
    PUBLISHED,
    CANCELED;

    public static boolean isValid(String eventState) {
        return EnumUtils.isValidEnum(
                EventState.class,
                eventState.toUpperCase()
        );
    }
}
