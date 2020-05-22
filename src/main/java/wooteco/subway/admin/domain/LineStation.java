package wooteco.subway.admin.domain;

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

    public boolean hasEqualStationID(Long stationId) {
        return Objects.equals(this.stationId, stationId);
    }

    public boolean hasEqualPreStationID(LineStation lineStation) {
        return Objects.equals(preStationId, lineStation.preStationId);
    }

    public boolean hasEqualPreStationID(Long stationId) {
        return Objects.equals(this.preStationId, stationId);
    }

    public boolean isFirstLineStation() {
        return preStationId == null;
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

    public void updatePreLineStation(Long preStationId) {
        this.preStationId = preStationId;
    }
}
