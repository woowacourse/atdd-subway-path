package wooteco.exception.unauthorized;

public class MemberAlreadyDeletedException extends UnauthorizedException {

    private static final String MESSAGE = "이미 삭제된 계정입니다.";

    public MemberAlreadyDeletedException() {
        super(MESSAGE);
    }
}
