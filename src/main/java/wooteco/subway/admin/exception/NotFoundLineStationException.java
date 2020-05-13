package wooteco.subway.admin.exception;

public class NotFoundLineStationException extends RuntimeException {
    private static final String NOT_FOUND_LINE_STATION_EXCEPTION_MESSAGE = "구간이 존재하지 않습니다.";
    private static final String NOT_FOUND_LINE_STATION_EXCEPTION_FORMAT = "%s -> %s 구간이 존재하지 않습니다.";

    public NotFoundLineStationException() {
        super(NOT_FOUND_LINE_STATION_EXCEPTION_MESSAGE);
    }

    public NotFoundLineStationException(String preStationName, String stationName) {
        super(String.format(NOT_FOUND_LINE_STATION_EXCEPTION_FORMAT, preStationName, stationName));
    }
}
