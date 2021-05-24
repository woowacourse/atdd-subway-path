package wooteco.subway.exception;

public class PathNotFoundException extends RuntimeException{
    private static final String MESSAGE = "경로를 찾지 못했습니다.";

    public PathNotFoundException() {
        super(MESSAGE);
    }

    public PathNotFoundException(String message) {
        super(message);
    }

    public PathNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
