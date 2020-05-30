package wooteco.subway.admin.exception;

public class StationNotFoundException extends RuntimeException {
    public StationNotFoundException(final String name) {
        super(name + "을 찾을 수 없습니다.");
    }
}
