package wooteco.subway.utils.exception;

public class StationNotFoundException extends RuntimeException {

    public StationNotFoundException() {
        super("[ERROR] 지하철역을 찾을 수 없습니다.");
    }
}
