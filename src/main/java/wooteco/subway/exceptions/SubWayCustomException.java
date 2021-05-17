package wooteco.subway.exceptions;

public class SubWayCustomException extends RuntimeException {

    private final SubWayException subWayException;

    public SubWayCustomException(SubWayException subWayException) {
        super(subWayException.message());
        this.subWayException = subWayException;
    }

    public String message() {
        return subWayException.message();
    }

    public int status() {
        return subWayException.status();
    }
}
