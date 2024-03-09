package ru.practicum.event.entity;

/**
 * <h3>Состояние запроса пользователя на участие в событии для участника</h3>
 * {@link #PENDING} - на модерации, <br/>
 * {@link #CONFIRMED} - подтверждена инициатором, <br/>
 * {@link #REJECTED} - отклонена инициатором, <br/>
 * {@link #CANCELED} - отменена заявителем  <br/>
 */
public enum RequestState {
    PENDING,
    CONFIRMED,
    REJECTED,
    CANCELED
}
