package wooteco.subway.auth.exception;

public class JwtLoginPasswordException extends AuthException {
    private static final String MESSAGE = "잘못된 패스워드임!";

    public JwtLoginPasswordException() {
        super(MESSAGE);
    }
}
