package ru.practicum.constants;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.exception.BadRequestException;

import static java.lang.String.format;

public interface Constants {

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
        REJECT_EVENT;

        public static StateAdminAction getInstance(String string) {
            try {
                return StateAdminAction.valueOf(string.toUpperCase());
            } catch (RuntimeException e) {
                throw new BadRequestException(format("Wrong action state for admin: %s", string));
            }
        }
    }

}
