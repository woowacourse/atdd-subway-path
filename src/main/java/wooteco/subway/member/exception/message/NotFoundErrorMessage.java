package wooteco.subway.member.exception.message;

import org.springframework.http.HttpStatus;

public enum NotFoundErrorMessage {
    MEMBER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다.");

    private HttpStatus httpStatus;
    private String message;

    NotFoundErrorMessage(HttpStatus httpStatus, String message) {
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
