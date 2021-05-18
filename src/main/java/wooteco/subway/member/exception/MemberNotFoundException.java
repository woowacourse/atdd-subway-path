package wooteco.subway.member.exception;

public class MemberNotFoundException extends AuthException{
    public MemberNotFoundException() {
    }

    public MemberNotFoundException(String message) {
        super(message);
    }
}
