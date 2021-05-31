package wooteco.subway.exception.badrequest;

public class DuplicateEmailException extends BadRequestException {
    private static final String MESSAGE = "중복된 이메일 입니다.";

    public DuplicateEmailException() {
        super(MESSAGE);
    }

    public DuplicateEmailException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
