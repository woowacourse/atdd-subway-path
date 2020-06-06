package wooteco.subway.admin.exception;

public class InvalidPathException extends RuntimeException {
    public static final String NO_CONNECTION_BETWEEN_START_END_STATIONS = "출발역과 도착역이 연결되어 있지 않습니다.";

    public InvalidPathException() {
        super(NO_CONNECTION_BETWEEN_START_END_STATIONS);
    }
}
