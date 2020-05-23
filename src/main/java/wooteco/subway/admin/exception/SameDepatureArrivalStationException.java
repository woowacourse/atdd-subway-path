package wooteco.subway.admin.exception;

public class SameDepatureArrivalStationException extends RuntimeException {
    public SameDepatureArrivalStationException() {
        super("출발역과 도착역이 같습니다.");
    }
}
