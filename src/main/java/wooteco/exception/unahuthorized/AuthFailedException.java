package wooteco.exception.unahuthorized;

public class AuthFailedException extends UnauthorizedException {

    public AuthFailedException() {
        super("인증 실패");
    }
}
