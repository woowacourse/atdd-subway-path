package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class SectionNotExistException extends ClientRuntimeException {

    public SectionNotExistException(final String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
