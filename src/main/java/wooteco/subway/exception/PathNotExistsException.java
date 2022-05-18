package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class PathNotExistsException extends ClientRuntimeException {

    private static final String MESSAGE = "출발역에서 도착역까지의 경로가 존재하지 않습니다.";

    public PathNotExistsException() {
        super(HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
