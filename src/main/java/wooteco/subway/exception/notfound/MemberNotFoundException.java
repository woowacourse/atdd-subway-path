package wooteco.subway.exception.notfound;

public class MemberNotFoundException extends NotFoundException {
    private static final String MESSAGE = "회원이 존재하지 않습니다.";

    public MemberNotFoundException() {
        super(MESSAGE);
    }

    public MemberNotFoundException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
