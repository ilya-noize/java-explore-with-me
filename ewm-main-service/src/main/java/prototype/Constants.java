package prototype;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.exception.BadRequestException;

public interface Constants {

    String FROM = "0";
    String SIZE = "10";
    String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    String NOT_EXIST = " with id:(%d) not exist";
    String CATEGORY_NOT_EXISTS = "Category" + NOT_EXIST;
    String COMPILATION_NOT_EXISTS = "Compilation" + NOT_EXIST;
    String EVENT_NOT_EXISTS = "Event" + NOT_EXIST;
    String EVENT_REQUEST_NOT_EXISTS = "Event request" + NOT_EXIST;
    String LOCATION_NOT_EXISTS = "Location" + NOT_EXIST;
    String USER_NOT_EXISTS = "User" + NOT_EXIST;

    static Pageable checkPageable(Integer from, Integer size) {
        if (from < 0 || size <= 0) {
            throw new BadRequestException("Pageable incorrect");
        }
        return PageRequest.of(from / size, size);
    }

    /**
     * Права пользователей
     */
    enum UserGroup {
        ROOT,
        USER,
        READ_ONLY,
        BAN
    }

    /**
     * Состояние запроса пользователя на участие в событии для инициатора
     */
    enum ParticipationRequestState {
        PENDING,
        CONFIRMED,
        REJECTED
    }

    /**
     * Состояние запроса пользователя на участие в событии для участника
     */
    enum RequestState {
        CONFIRMED,
        REJECTED
    }
}
