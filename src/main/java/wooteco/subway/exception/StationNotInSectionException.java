package wooteco.subway.exception;

public class StationNotInSectionException extends IllegalArgumentException{

    public StationNotInSectionException(String message) {
        super(message);
    }
}
