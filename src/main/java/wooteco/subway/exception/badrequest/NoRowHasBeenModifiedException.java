package wooteco.subway.exception.badrequest;

public class NoRowHasBeenModifiedException extends BadRequestException {

    public NoRowHasBeenModifiedException(String message) {
        super(message);
    }

    public NoRowHasBeenModifiedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
