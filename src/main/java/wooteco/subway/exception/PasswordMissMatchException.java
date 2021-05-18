package wooteco.subway.exception;

public class PasswordMissMatchException extends AuthorizationException {
    private static final String MESSAGE = "비밀번호가 일치하지 않습니다.";

    public PasswordMissMatchException() {
        super(MESSAGE);
    }
}
