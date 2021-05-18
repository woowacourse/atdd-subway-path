package wooteco.subway.member.exception;

public class NotFoundException extends RuntimeException{

    private ErrorMessage errorMessage;

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        this.errorMessage = new ErrorMessage(message);
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
