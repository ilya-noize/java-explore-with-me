package ru.practicum.event.entity;

/**
 * <h3>Состояние события при обновлении</h3>
 * {@link #SEND_TO_REVIEW} - на модерации, <br/>
 * {@link #CANCEL_REVIEW} - снята с модерации <br/>
 */
public enum StateAction {
    SEND_TO_REVIEW,
    CANCEL_REVIEW
}
