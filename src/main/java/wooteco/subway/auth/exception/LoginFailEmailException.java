package wooteco.subway.auth.exception;

public class LoginFailEmailException extends LoginFailException {

    public LoginFailEmailException() {
        super("해당 이메일이 존재하지 않습니다.");
    }
}
