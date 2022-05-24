package wooteco.subway.exception;

public class LowFareException extends RuntimeException {
    public LowFareException(String message) {
        super(message);
    }
}
