package wooteco.subway.admin.controller.exceptionhandler.validator;

public enum ErrorCode {
    INVALID_INPUT_VALUE(400, "입력값이 유효하지 않습니다!");

    private final int status;
    private final String message;

    ErrorCode(final int status, final String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
