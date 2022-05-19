package wooteco.subway.exception;

public class StationNotFoundException extends BusinessException {
    public StationNotFoundException(Long id) {
        super(String.format("%d 역이 존재하지 않습니다", id));
    }
}
