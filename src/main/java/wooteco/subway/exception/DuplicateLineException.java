package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public final class DuplicateLineException extends SubwayException {

    public DuplicateLineException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
