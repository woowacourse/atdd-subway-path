package wooteco.subway.admin.dto;

public class ErrorResponse {

    private String message;

    private ErrorResponse() {}

    private ErrorResponse(String message) {
        this.message = message;
    }

    public static ErrorResponse of(Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    public String getMessage() {
        return message;
    }
}
