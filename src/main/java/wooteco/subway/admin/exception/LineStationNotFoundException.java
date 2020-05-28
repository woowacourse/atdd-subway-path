package wooteco.subway.admin.exception;

public class LineStationNotFoundException extends RuntimeException {
    public LineStationNotFoundException() {
        super("해당 노선에 입력하신 역이 존재하지 않습니다");
    }
}
