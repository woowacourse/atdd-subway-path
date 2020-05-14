package wooteco.subway.admin.exception;

public class NotFoundStationException extends IllegalArgumentException {

    public static final String ERROR_MESSAGE = "존재하지 않는 역입니다.";

    public NotFoundStationException() {
        super(ERROR_MESSAGE);
    }
}
