package wooteco.subway.exception;

public class RequestInvalidException extends SubwayBadRequestException {

    private static final String MESSAGE = "입력정보가 유효하지 않습니다.";

    public RequestInvalidException() {
        super(MESSAGE);
    }
}
