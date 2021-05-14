package wooteco.subway.exception.auth;

public enum AuthExceptionStatus {
    INVALID_ACCESS("가입하지 않은 이메일이거나, 잘못된 비밀번호입니다."),
    EXPIRED_TOKEN("유효하지 않은 접근입니다.");

    private final String message;

    AuthExceptionStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
