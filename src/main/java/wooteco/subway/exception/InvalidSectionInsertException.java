package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class InvalidSectionInsertException extends SubwayException {

    public InvalidSectionInsertException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
