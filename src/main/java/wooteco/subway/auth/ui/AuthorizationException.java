package wooteco.subway.auth.ui;

public class AuthorizationException extends RuntimeException {
    private static String MESSAGE = "인증에 실패했습니다.";

    public AuthorizationException() {
        super(MESSAGE);
    }
}
