package wooteco.subway.ui.dto;

public class ExceptionResponse {

    private String errorMessage;

    private ExceptionResponse() {
    }

    public ExceptionResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
