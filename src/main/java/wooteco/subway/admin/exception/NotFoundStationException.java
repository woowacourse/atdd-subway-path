package wooteco.subway.admin.exception;

public class NotFoundStationException extends RuntimeException {
    private static final String NOT_FOUND_STATION_EXCEPTION_MESSAGE = "지하철 역이 존재하지 않습니다.";
    private static final String NOT_FOUND_STATION_EXCEPTION_FORMAT = "%s 에 해당하는 역이 존재하지 않습니다.";

    public NotFoundStationException() {
        super(NOT_FOUND_STATION_EXCEPTION_MESSAGE);
    }

    public NotFoundStationException(String name) {
        super(String.format(NOT_FOUND_STATION_EXCEPTION_FORMAT, name));
    }
}
