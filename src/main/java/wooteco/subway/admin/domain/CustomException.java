package wooteco.subway.admin.domain;

import java.util.Objects;

public class CustomException extends RuntimeException {
    private String message;
    private RuntimeException exception;

    public CustomException(String message, RuntimeException exception) {
        super(getRuntimeMessage(message, exception));
        this.message = message;
        this.exception = exception;
    }

    private static String getRuntimeMessage(String message, RuntimeException exception) {
        if (Objects.isNull(exception.getMessage()) || exception.getMessage().isEmpty()) {
            return message;
        }
        return exception.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
