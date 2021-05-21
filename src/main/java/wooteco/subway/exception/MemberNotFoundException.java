package wooteco.subway.exception;

public class MemberNotFoundException extends AuthException {
    public MemberNotFoundException() {
    }

    public MemberNotFoundException(String info) {
        super(String.format("사용자를 찾을 수 없습니다. [%s]", info));
    }
}
