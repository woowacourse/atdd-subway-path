package wooteco.subway.exception;

import wooteco.subway.auth.application.AuthorizationException;

public class InvalidEmailException extends AuthorizationException {
    private final static String MESSAGE = "유효하지 않은 이메일 입니다";

    public InvalidEmailException() {
        super(MESSAGE);
    }
}
