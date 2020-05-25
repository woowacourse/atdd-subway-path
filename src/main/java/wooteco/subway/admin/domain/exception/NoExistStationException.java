package wooteco.subway.admin.domain.exception;

public class NoExistStationException extends IllegalArgumentException {
    private static final String NO_EXIST_STATION_ERR_MSG = "존재하지 않는 역은 입력할 수 없습니다.";

    public NoExistStationException() {
        super(NO_EXIST_STATION_ERR_MSG);
    }
}
