package wooteco.subway.auth.application;

public class PasswordInvalidException extends RuntimeException {
    private static String MESSAGE = "비밀번호가 일치하지 않습니다.";

    public PasswordInvalidException() {
        super(MESSAGE);
    }
}
