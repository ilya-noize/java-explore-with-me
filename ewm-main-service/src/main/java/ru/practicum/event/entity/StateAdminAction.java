package ru.practicum.event.entity;

/**
 * <h3>Состояние события от администратора/модератора</h3>
 * {@link #PUBLISH_EVENT} - подтверждена модератором, <br/>
 * {@link #REJECT_EVENT} - отклонена модератором <br/>
 */
public enum StateAdminAction {
    PUBLISH_EVENT,
    REJECT_EVENT
}
