package wooteco.auth.exception.notFound;

public class MemberNotFoundException extends NotFoundException {

    private static final String MESSAGE = "존재하지 않는 멤버입니다.";

    public MemberNotFoundException() {
        super(MESSAGE);
    }


}
