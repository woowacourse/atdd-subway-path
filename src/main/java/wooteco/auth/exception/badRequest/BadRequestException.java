package wooteco.auth.exception.badRequest;

public class BadRequestException extends RuntimeException {

    private final ErrorResponse errorResponse;

    public BadRequestException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public ErrorResponse errorResponse() {
        return errorResponse;
    }
}
