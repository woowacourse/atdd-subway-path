package wooteco.subway.exception.auth;

public class LoginWrongPasswordException extends LoginFailException {

    public LoginWrongPasswordException() {
        super("해당 비밀번호가 틀렸습니다.");
    }
}
