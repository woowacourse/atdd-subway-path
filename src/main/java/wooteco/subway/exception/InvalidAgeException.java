package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class InvalidAgeException extends SubwayException {

    public InvalidAgeException() {
        super("유효한 나이 값이 아닙니다.", HttpStatus.BAD_REQUEST);
    }
}
