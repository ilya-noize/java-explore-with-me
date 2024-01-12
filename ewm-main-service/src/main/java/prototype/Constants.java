package prototype;

public interface Constants {
    String FROM = "0";
    String SIZE = "10";
    String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    enum ParticipationRequestState {
        PENDING,
        CONFIRMED,
        REJECTED
    }

    enum EventState {
        PENDING,
        PUBLISHED,
        CANCELED
    }
}
