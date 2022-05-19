package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public final class DuplicateStationException extends SubwayException {

    public DuplicateStationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
