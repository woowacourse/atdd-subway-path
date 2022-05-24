package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class DuplicatedException extends SubwayException {

    public DuplicatedException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
