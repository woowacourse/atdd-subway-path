package wooteco.subway.dto;

public class ErrorMessageResponse {

    private String message;

    private ErrorMessageResponse() {
    }

    public ErrorMessageResponse(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
