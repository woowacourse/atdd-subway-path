package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public final class NotFoundLineException extends SubwayException {

    public NotFoundLineException(final String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
