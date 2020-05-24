package wooteco.subway.domain;

import java.util.Objects;

public class LineStation {
    private Long preStationId;
    private Long stationId;
    private int distance;
    private int duration;

    public LineStation(Long preStationId, Long stationId, int distance, int duration) {
        this.preStationId = preStationId;
        this.stationId = stationId;
        this.distance = distance;
        this.duration = duration;
    }

    void updatePreLineStation(Long preStationId) {
        this.preStationId = preStationId;
    }

    boolean isFirstStation() {
        return Objects.isNull(preStationId);
    }

    boolean isNotFirstStation() {
        return !isFirstStation();
    }

    boolean isPreStationOf(LineStation lineStation) {
        return stationId.equals(lineStation.preStationId);
    }

    public Long getPreStationId() {
        return preStationId;
    }

    public Long getStationId() {
        return stationId;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }
}
