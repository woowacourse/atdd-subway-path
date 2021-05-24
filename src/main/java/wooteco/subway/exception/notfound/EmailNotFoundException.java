package wooteco.subway.exception.notfound;

public class EmailNotFoundException extends NotFoundException {
    private static final String MESSAGE = "이메일이 존재하지 않습니다.";

    public EmailNotFoundException() {
        super(MESSAGE);
    }

    public EmailNotFoundException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
