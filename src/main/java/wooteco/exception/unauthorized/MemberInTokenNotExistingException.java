package wooteco.exception.unauthorized;

public class MemberInTokenNotExistingException extends UnauthorizedException {

    private static final String MESSAGE = "해당 토큰에 등록된 사용자는 존재하지 않습니다.";

    public MemberInTokenNotExistingException() {
        super(MESSAGE);
    }
}
