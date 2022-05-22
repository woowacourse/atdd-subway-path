package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class DuplicateLineException extends SubwayException {

    public DuplicateLineException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
