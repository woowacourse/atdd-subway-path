package wooteco.subway.exception.auth;

public class LoginFailEmailException extends LoginFailException {

    public LoginFailEmailException() {
        super("[ERROR] 해당 이메일이 존재하지 않습니다.");
    }
}
