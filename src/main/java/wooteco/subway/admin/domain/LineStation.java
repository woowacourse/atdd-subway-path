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

    public boolean isEdgeOf(Long preStationId, Long stationId) {
        return Objects.equals(this.preStationId, preStationId) && Objects.equals(this.stationId, stationId);
    }

    public static LineStation empty() {
        return new LineStation(null, null, 0, 0);
    }
}
