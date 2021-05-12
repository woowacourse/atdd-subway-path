package wooteco.exception.badRequest;

public class PasswordIncorrectException extends BadRequestException {

    public static final String MESSAGE = "비밀번호가 잘못되었습니다.";

    public PasswordIncorrectException() {
        super(MESSAGE);
    }
}
