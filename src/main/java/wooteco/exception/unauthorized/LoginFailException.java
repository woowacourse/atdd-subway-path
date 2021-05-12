package wooteco.exception.unauthorized;

public class LoginFailException extends UnauthorizedException {

    public static final String MESSAGE = "로그인을 실패했습니다.";

    public LoginFailException() {
        super(MESSAGE);
    }
}
