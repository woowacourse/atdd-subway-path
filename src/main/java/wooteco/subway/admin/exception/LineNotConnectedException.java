package wooteco.subway.admin.exception;

public class LineNotConnectedException extends IllegalArgumentException {
    public LineNotConnectedException() {
        super("출발역과 도착역이 연결되어 있지 않습니다!");
    }
}
