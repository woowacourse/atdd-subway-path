package wooteco.auth.exception.badRequest;

public class PasswordIncorrectException extends BadRequestException {

    public PasswordIncorrectException(ErrorResponse errorResponse) {
        super(errorResponse);
    }
}
