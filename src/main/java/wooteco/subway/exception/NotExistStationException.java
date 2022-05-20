package wooteco.subway.exception;

public class NotExistStationException extends IllegalArgumentException {

    public NotExistStationException(String message) {
        super(message);
    }
}
