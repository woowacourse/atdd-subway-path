package wooteco.subway.admin.exception;

public class DisconnectedStationException extends RuntimeException {
    public DisconnectedStationException(Long id) {
        super(String.format("id가 %d인 역의 다음역이 존재하지 않습니다", id));
    }
}
