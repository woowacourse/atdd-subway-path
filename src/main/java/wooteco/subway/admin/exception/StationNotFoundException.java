package wooteco.subway.admin.exception;

public class StationNotFoundException extends RuntimeException {
    public StationNotFoundException() {
        super("지하철 역이 존재하지 않습니다.");
    }

    public StationNotFoundException(String stationName) {
        super(stationName + "역을 찾을 수 없습니다.");
    }
}
