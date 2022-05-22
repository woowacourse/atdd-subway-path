package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class NotFoundStationException extends SubwayException {

    public NotFoundStationException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
