package wooteco.subway.auth.exception;

import org.springframework.http.HttpStatus;

public class UserLoginFailException extends RuntimeException {

    private static final int STATUS_CODE = HttpStatus.UNAUTHORIZED.value();

    public UserLoginFailException() {
        super("로그인 정보가 맞지 않습니다.");
    }

    public int getStatusCode() {
        return STATUS_CODE;
    }
}
