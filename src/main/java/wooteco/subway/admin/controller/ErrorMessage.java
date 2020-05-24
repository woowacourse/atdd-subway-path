package wooteco.subway.admin.controller;

public class ErrorMessage {
    private String message;

    private ErrorMessage() {
    }

    public ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
