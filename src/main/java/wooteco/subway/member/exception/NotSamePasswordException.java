package wooteco.subway.member.exception;

public class NotSamePasswordException extends MemberException {
    private static final String MESSAGE = "유효하지 않은 비밀번호임!";

    public NotSamePasswordException() {
        super(MESSAGE);
    }
}
