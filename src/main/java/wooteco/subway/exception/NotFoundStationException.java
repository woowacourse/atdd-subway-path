package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public final class NotFoundStationException extends SubwayException{

    public NotFoundStationException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
