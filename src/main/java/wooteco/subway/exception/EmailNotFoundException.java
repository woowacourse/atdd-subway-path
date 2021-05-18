package wooteco.subway.exception;

public class EmailNotFoundException extends AuthorizationException {
    private static final String MESSAGE = "이메일이 존재하지 않습니다.";

    public EmailNotFoundException() {
        super(MESSAGE);
    }
}
