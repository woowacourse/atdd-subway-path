package wooteco.subway.admin.exception;

public class InvalidLineException extends IllegalArgumentException {
    public InvalidLineException(String message) {
        super(message);
    }
}
