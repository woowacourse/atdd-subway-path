package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public final class NotFoundPathException extends SubwayException {

    public NotFoundPathException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
