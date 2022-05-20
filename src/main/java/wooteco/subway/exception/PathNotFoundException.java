package wooteco.subway.exception;

public class PathNotFoundException extends IllegalArgumentException {

    public PathNotFoundException(String message) {
        super(message);
    }
}
