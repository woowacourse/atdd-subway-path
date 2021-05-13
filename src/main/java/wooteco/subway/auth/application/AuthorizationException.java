package wooteco.subway.auth.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthorizationException extends RuntimeException {
    private static final String MESSAGE = "토근인증에 실패했습니다.";
    public AuthorizationException() {
        super(MESSAGE);
    }
}
