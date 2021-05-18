package wooteco.subway.station.application;

public class StationNotFoundException extends RuntimeException {

    public StationNotFoundException(String message) {
        super(message);
    }

    public StationNotFoundException() {
        this("존재하지 않는 역 id입니다.");
    }
}
