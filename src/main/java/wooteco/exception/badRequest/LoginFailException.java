package wooteco.exception.badRequest;

public class LoginFailException extends BadRequestException {

    public static final String MESSAGE = "일치하는 아이디 또는 비밀번호를 찾을 수 없습니다.";

    public LoginFailException() {
        super(MESSAGE);
    }
}
