package wooteco.subway.auth.exception;

public enum AuthError {
    LOGIN_ERROR(401, "로그인에 실패하셨습니다"),
    TOKEN_EXPIRED(401, "토큰이 만료되었습니다"),
    EMAIL_NOT_FOUND_ERROR(401, "토큰의 이메일이 유효하지 않습니다");

    private int statusCode;
    private String message;

    AuthError(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
