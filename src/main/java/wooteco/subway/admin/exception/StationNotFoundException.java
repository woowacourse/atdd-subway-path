package wooteco.subway.admin.exception;

public class StationNotFoundException extends RuntimeException {

    public static final String STATION_NOT_FOUND_EXCEPTION_MESSAGE = "STATION_NOT_FOUND";

    public StationNotFoundException() {
        super(STATION_NOT_FOUND_EXCEPTION_MESSAGE);
    }
}
