package wooteco.subway.admin.exception;

public class VerticesNotConnectedException extends RuntimeException {
    public VerticesNotConnectedException() {
        super("입력하신 역이 연결되어있지 않습니다.");
    }
}
