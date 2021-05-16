package wooteco.subway.exception.auth;

public class LoginFailException extends RuntimeException {

    public LoginFailException(String message) {
        super(message);
    }
}
