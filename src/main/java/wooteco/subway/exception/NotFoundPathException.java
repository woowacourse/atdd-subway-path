package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class NotFoundPathException extends SubwayException {

    public NotFoundPathException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
