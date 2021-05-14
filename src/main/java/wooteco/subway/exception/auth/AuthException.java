package wooteco.subway.exception.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import wooteco.subway.exception.SubwayException;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "인증 오류")
public class AuthException extends SubwayException {
    public AuthException(String message) {
        super(message);
    }
}
