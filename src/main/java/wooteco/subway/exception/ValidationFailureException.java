package wooteco.subway.exception;

public class ValidationFailureException extends BusinessRelatedException {

    public ValidationFailureException(String message) {
        super(message);
    }
}
