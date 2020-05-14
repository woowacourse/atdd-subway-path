package wooteco.subway.admin.exception;

public class OverlappedStationException extends IllegalArgumentException {
    private static final String message = "입력된 시작역과 끝역이 동일합니다.";

    public OverlappedStationException() {
        super(message);
    }
}
