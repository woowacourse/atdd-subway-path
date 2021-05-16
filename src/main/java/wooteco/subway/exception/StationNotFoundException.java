package wooteco.subway.exception;

public class StationNotFoundException extends SubwayNotFoundException {
    private static final String MESSAGE = "역이 존재하지 않습니다.";

    public StationNotFoundException() {
        super(MESSAGE);
    }
}
