package wooteco.subway.exception;

public class StationNotFoundException extends SubwayException {
    private static final String STATION_NOT_FOUND_ERROR_MESSAGE = "해당 역을 찾을 수 없습니다.";
    public StationNotFoundException() {
        super(STATION_NOT_FOUND_ERROR_MESSAGE);
    }
}
