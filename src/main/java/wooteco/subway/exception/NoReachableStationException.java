package wooteco.subway.exception;

public class NoReachableStationException extends IllegalArgumentException{

    public NoReachableStationException(String message) {
        super(message);
    }
}
