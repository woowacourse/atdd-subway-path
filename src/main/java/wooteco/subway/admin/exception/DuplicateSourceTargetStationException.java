package wooteco.subway.admin.exception;

public class DuplicateSourceTargetStationException extends RuntimeException {
    public static final String SAME_SOURCE_TARGET_STATION_NAME = "출발역과 도착역은 같을 수 없습니다.";

    public DuplicateSourceTargetStationException() {
        super(SAME_SOURCE_TARGET_STATION_NAME);
    }
}
