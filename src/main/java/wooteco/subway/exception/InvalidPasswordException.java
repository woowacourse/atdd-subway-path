package wooteco.subway.exception;

import wooteco.subway.auth.application.AuthorizationException;

public class InvalidPasswordException extends AuthorizationException {
    private final static String MESSAGE = "유요하지 않은 비밀번호 입니다";

    public InvalidPasswordException() {
        super(MESSAGE);
    }
}
