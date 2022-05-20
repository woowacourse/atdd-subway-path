package wooteco.subway.exception;

public class AllStationsNotInSectionException extends IllegalArgumentException{

    public AllStationsNotInSectionException(String message) {
        super(message);
    }
}
