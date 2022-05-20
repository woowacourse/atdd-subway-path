package wooteco.subway.exception;

public class StationDuplicationException extends IllegalArgumentException{

    public StationDuplicationException(String message) {
        super(message);
    }
}
