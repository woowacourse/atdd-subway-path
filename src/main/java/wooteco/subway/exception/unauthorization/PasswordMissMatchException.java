package wooteco.subway.exception.unauthorization;

public class PasswordMissMatchException extends UnAuthorizationException {
    private static final String MESSAGE = "비밀번호가 일치하지 않습니다.";

    public PasswordMissMatchException() {
        super(MESSAGE);
    }
}
