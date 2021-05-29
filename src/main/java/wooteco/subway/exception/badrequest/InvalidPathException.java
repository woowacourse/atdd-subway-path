package wooteco.subway.exception.badrequest;

public class InvalidPathException extends BadRequestException {

    public InvalidPathException(String message) {
        super(message);
    }

    public InvalidPathException(String message, Throwable cause) {
        super(message, cause);
    }
}