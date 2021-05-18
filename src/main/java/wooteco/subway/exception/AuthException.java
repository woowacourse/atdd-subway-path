package wooteco.subway.exception;

public class AuthException extends RuntimeException {
    private ErrorMessage errorMessage;

    public AuthException() {
    }

    public AuthException(String message) {
        this.errorMessage = new ErrorMessage(message);
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
