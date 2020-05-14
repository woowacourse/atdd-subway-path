package wooteco.subway.admin.exception;

public class NotFoundPathException extends RuntimeException {
    private static final String ERROR_MESSAGE = "경로가 존재하지 않습니다.";

    public NotFoundPathException() {
        super(ERROR_MESSAGE);
    }
}
