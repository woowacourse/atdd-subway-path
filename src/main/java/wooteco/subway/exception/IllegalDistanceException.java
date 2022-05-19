package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public final class IllegalDistanceException extends SubwayException {

    public IllegalDistanceException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
