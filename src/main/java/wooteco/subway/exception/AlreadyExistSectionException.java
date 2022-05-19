package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public final class AlreadyExistSectionException extends SubwayException {

    public AlreadyExistSectionException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
