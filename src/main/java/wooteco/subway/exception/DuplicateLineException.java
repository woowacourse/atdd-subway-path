package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public final class DuplicateLineException extends SubwayException {

    public DuplicateLineException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
