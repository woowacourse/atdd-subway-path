package wooteco.subway.exception;

public class MemberNotFoundException extends AuthException {
    public MemberNotFoundException() {
    }

    public MemberNotFoundException(String message) {
        super(message);
    }
}
