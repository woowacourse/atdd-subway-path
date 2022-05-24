package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends SubwayException {

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
