package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class DuplicatedSourceAndTargetException extends ClientRuntimeException {

    public DuplicatedSourceAndTargetException(final String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
