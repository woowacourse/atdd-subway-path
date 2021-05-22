package wooteco.subway.exception.path;

public class SubwayMapException extends RuntimeException {
    private static final String MESSAGE = "존재하지 않는 역에 대해 경로를 조회할 수 없습니다.";

    public SubwayMapException() {
        super(MESSAGE);
    }
}
