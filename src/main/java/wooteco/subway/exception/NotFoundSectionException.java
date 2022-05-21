package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class NotFoundSectionException extends SubwayException {

    public NotFoundSectionException(final String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
