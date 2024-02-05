package ru.practicum.constants;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.exception.BadRequestException;


public interface Constants {

    int MIN_COMPILATION_TITLE_LENGTH = 3;
    int MAX_COMPILATION_TITLE_LENGTH = 50;

    int MIN_EVENT_TITLE_LENGTH = 3;
    int MAX_EVENT_TITLE_LENGTH = 120;
    int MIN_EVENT_ANNOTATION_LENGTH = 20;
    int MAX_EVENT_ANNOTATION_LENGTH = 2000;
    int MIN_EVENT_DESCRIPTION_LENGTH = 20;
    int MAX_EVENT_DESCRIPTION_LENGTH = 7000;

    int MIN_CATEGORY_NAME_LENGTH = 3;
    int MAX_CATEGORY_NAME_LENGTH = 50;

    int MIN_COMPILATION_NAME_LENGTH = 3;
    int MAX_COMPILATION_NAME_LENGTH = 50;

    int MIN_USER_NAME_LENGTH = 2;
    int MIN_USER_EMAIL_LENGTH = 6;
    int MAX_USER_NAME_LENGTH = 250;
    int MAX_USER_EMAIL_LENGTH = 254;

    String FROM = "0";
    String SIZE = "10";

    String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    String NOT_EXIST = " with id:(%d) not exist";
    String CATEGORY_NOT_EXISTS = "Category" + NOT_EXIST;
    String COMPILATION_NOT_EXISTS = "Compilation" + NOT_EXIST;
    String EVENT_NOT_EXISTS = "Event" + NOT_EXIST;
    String EVENT_REQUEST_NOT_EXISTS = "Event request" + NOT_EXIST;
    String USER_NOT_EXISTS = "User" + NOT_EXIST;

    static Pageable checkPageable(Integer from, Integer size, Sort sort) {
        if (from == null) from = Integer.parseInt(FROM);
        if (size == null) size = Integer.parseInt(SIZE);
        if (from < 0 || size <= 0) {
            throw new BadRequestException("Pageable incorrect");
        }
        if (sort != null) {
            return PageRequest.of(from / size, size, sort);
        }
        return PageRequest.of(from / size, size);
    }

    /**
     * <h3>Состояние события от администратора/модератора</h3>
     * {@link #PUBLISH_EVENT} - подтверждена модератором, <br/>
     * {@link #REJECT_EVENT} - отклонена модератором <br/>
     */
    enum StateAdminAction {
        PUBLISH_EVENT,
        REJECT_EVENT
    }

    /**
     * <h3>Состояние события</h3>
     * {@link #PENDING} - на модерации, <br/>
     * {@link #PUBLISHED} - подтверждена модератором, <br/>
     * {@link #CANCELED} - отклонена инициатором, <br/>
     *
     * {@link #isValid(String)} - проверка строки на соответствие набору статусов <br/>
     */
    enum EventState {
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

    /**
     * <h3>Сортировка событий в поиске</h3>
     * {@link #EVENT_DATE} - по дате начала, <br/>
     * {@link #VIEWS} - по количеству просмотром <br/>
     */
    enum EventSortState {
        EVENT_DATE,
        VIEWS
    }

    /**
     * <h3>Состояние события при обновлении</h3>
     * {@link #SEND_TO_REVIEW} - на модерации, <br/>
     * {@link #CANCEL_REVIEW} - снята с модерации <br/>
     */
    enum StateAction {
        SEND_TO_REVIEW,
        CANCEL_REVIEW
    }

    /**
     * <h3>Состояние запроса пользователя на участие в событии для участника</h3>
     * {@link #PENDING} - на модерации, <br/>
     * {@link #CONFIRMED} - подтверждена инициатором, <br/>
     * {@link #REJECTED} - отклонена инициатором, <br/>
     * {@link #CANCELED} - отменена заявителем  <br/>
     */
    enum RequestState {
        PENDING,
        CONFIRMED,
        REJECTED,
        CANCELED
    }
}
