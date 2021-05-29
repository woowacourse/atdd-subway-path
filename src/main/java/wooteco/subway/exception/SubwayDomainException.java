package wooteco.subway.exception;

public abstract class SubwayDomainException extends RuntimeException {
    public SubwayDomainException(String message) {
        super(message);
    }
}
