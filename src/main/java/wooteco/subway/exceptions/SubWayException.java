package wooteco.subway.exceptions;

public class SubWayException extends RuntimeException {

    private final SubWayExceptionSet subWayExceptionSet;

    public SubWayException(SubWayExceptionSet subWayExceptionSet) {
        super(subWayExceptionSet.message());
        this.subWayExceptionSet = subWayExceptionSet;
    }

    public String message() {
        return subWayExceptionSet.message();
    }

    public int status() {
        return subWayExceptionSet.status();
    }
}
