package wooteco.subway.exception.auth;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;

public class AuthException extends SubwayException {
    public AuthException(HttpStatus httpStatus, Object body) {
        super(httpStatus, body);
    }
}