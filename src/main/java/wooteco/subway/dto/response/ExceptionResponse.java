package wooteco.subway.dto.response;

public class ExceptionResponse {
    private final String message;

    private ExceptionResponse() {
        this(null);
    }

    private ExceptionResponse(String message) {
        this.message = message;
    }

    public static ExceptionResponse of(Exception exception) {
        return new ExceptionResponse(exception.getMessage());
    }

    public String getMessage() {
        return message;
    }
}
