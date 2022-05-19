package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public final class NotFoundSectionException extends SubwayException{

    public NotFoundSectionException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
