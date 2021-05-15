package wooteco.subway.member.application;

public class MemberException extends IllegalArgumentException {

    public MemberException() {
        super("적절하지 않은 유저입니다.");
    }

    public MemberException(final String message) {
        super(message);
    }
}
