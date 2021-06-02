package wooteco.subway.auth.exception;

import wooteco.subway.exception.web.UnauthorizedException;

public class AuthorizationFailureException extends UnauthorizedException {

    public AuthorizationFailureException(String message) {
        super(String.format("인증에 실패했습니다. (%s)", message));
    }
}
