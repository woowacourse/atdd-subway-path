package wooteco.subway.auth.exception;

public class JwtLoginEmailException extends AuthException {
    private static final String MESSAGE = "없는 이메일임!";

    public JwtLoginEmailException() {
        super(MESSAGE);
    }
}
