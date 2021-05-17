package wooteco.subway.auth.exception;

import org.springframework.http.HttpStatus;

public enum AuthError {
    LOGIN_ERROR(HttpStatus.UNAUTHORIZED, "로그인에 실패하셨습니다"),
    TOKEN_EXPIRED_ERROR(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다"),
    EMAIL_NOT_FOUND_ERROR(HttpStatus.UNAUTHORIZED, "토큰의 이메일이 유효하지 않습니다");

    private HttpStatus status;
    private String message;

    AuthError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatusCode() {
        return status.value();
    }

    public String getMessage() {
        return message;
    }
}
