package wooteco.subway.station.exception;

public class SameStationException extends StationException{

    public SameStationException(Long stationId) {
        super(String.valueOf(stationId) + "의 역은 겹치는 역입니다.");
    }
}
