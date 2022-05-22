package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class DuplicateStationException extends SubwayException {

    public DuplicateStationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
