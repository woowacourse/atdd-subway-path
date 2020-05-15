package wooteco.subway.admin.dto.error;

public class ErrorResponse {
    private final Integer status;
    private final String message;

    public ErrorResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ErrorResponse of(Integer status, String message) {
        return new ErrorResponse(status, message);
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
