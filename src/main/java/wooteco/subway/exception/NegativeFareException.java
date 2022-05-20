package wooteco.subway.exception;

public class NegativeFareException extends InvalidRequestException {
    public NegativeFareException(String message) {
        super(message);
    }
}
