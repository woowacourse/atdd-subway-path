package wooteco.subway.admin.exception;

public class NotFoundStationException extends RuntimeException {
    private static final String NO_EXIST_FIRST_STATION = "첫번째 지하철역이 존재하지 않습니다.";

    public NotFoundStationException() {
        super(NO_EXIST_FIRST_STATION);
    }

    public NotFoundStationException(Long id) {
        super(String.format("id가 %d인 역이 존재하지 않습니다", id));
    }
}
