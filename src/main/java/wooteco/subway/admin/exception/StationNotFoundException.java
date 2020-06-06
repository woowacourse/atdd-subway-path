package wooteco.subway.admin.exception;

public class StationNotFoundException extends RuntimeException {

    public static final String STATION_NOT_FOUND_EXCEPTION_MESSAGE = "존재하지 않는 역입니다.";

    public StationNotFoundException() {
        super(STATION_NOT_FOUND_EXCEPTION_MESSAGE);
    }
}
