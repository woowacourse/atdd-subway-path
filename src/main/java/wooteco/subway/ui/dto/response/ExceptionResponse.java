package wooteco.subway.ui.dto.response;

public class ExceptionResponse {

    private final boolean success;
    private final String message;

    private ExceptionResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static ExceptionResponse from(String message) {
        return new ExceptionResponse(false, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
