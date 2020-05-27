package wooteco.subway.admin.exception;

public class NotFoundPathException extends RuntimeException {
    private static final String NOT_FOUND_PATH_EXCEPTION_MESSAGE = "경로를 찾을 수 없습니다";
    private static final String NOT_FOUND_PATH_EXCEPTION_FORMAT = "%s에서 %s로 가는 경로를 찾을 수 없습니다";

    private NotFoundPathException() {
        super(NOT_FOUND_PATH_EXCEPTION_MESSAGE);
    }

    public NotFoundPathException(String sourceName, String targetName) {
        super(String.format(NOT_FOUND_PATH_EXCEPTION_FORMAT, sourceName, targetName));
    }
}
