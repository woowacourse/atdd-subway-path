package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class NotFoundLineException extends SubwayException {

    public NotFoundLineException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
