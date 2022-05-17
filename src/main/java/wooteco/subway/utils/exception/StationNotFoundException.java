package wooteco.subway.utils.exception;

public class StationNotFoundException extends NotFoundException {

    public StationNotFoundException() {
        super("[ERROR] 지하철역을 찾을 수 없습니다.");
    }
}
