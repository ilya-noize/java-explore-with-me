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
     * Состояние события от администратора/модератора
     */
    enum StateAdminAction {
        PUBLISH_EVENT,
        REJECT_EVENT
    }

    /**
     * Состояние события
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
     * Сортировка событий в поиске
     */
    enum EventSortState {
        EVENT_DATE,
        VIEWS
    }

    /**
     * Состояние события при обновлении
     */
    enum StateAction {
        SEND_TO_REVIEW,
        CANCEL_REVIEW
    }

    /**
     * Состояние запроса пользователя на участие в событии для участника
     */
    enum RequestState {
        PENDING,
        CONFIRMED,
        REJECTED,
        CANCELED
    }
}
