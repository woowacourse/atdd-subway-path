package wooteco.subway.member.exception.message;

import org.springframework.http.HttpStatus;

public enum AuthErrorMessage {
    LOGIN_ERROR(HttpStatus.UNAUTHORIZED, "로그인에 실패하였습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    TOKEN_NOT_VALID(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다.");

    private HttpStatus httpStatus;
    private String message;

    AuthErrorMessage(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
