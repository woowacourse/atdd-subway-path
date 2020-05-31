package wooteco.subway.admin.exception;

public class NotConnectEdgeException extends RuntimeException {
    public NotConnectEdgeException() {
        super("출발 지점으로부터 도착 지점까지 갈 수 없습니다!");
    }
}
