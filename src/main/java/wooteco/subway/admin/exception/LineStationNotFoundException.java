package wooteco.subway.admin.exception;

public class LineStationNotFoundException extends RuntimeException {
    public LineStationNotFoundException() {
        super("노선에 속한 역이 존재하지 않습니다.");
    }
}
