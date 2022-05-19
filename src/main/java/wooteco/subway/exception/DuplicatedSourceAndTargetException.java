package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class DuplicatedSourceAndTargetException extends ClientRuntimeException {

    private static final String MESSAGE = "출발역과 도착역은 같을 수 없습니다.";

    public DuplicatedSourceAndTargetException() {
        super(HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
