package wooteco.subway.auth.exception;

public class UserLoginFailException extends RuntimeException {

    private static final int STATUS_CODE = HttpStatus.UNAUTHORIZED.value();

    public UserLoginFailException() {
        super("[ERROR] 로그인 정보가 맞지 않습니다.");
    }

    public int getStatusCode() {
        return STATUS_CODE;
    }
}
