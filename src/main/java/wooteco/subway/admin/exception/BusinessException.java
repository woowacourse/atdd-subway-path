package wooteco.subway.admin.exception;

public class BusinessException extends RuntimeException {
    private final String message;
    private final int status;

    public BusinessException(ErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}

