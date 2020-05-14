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

    public void updatePreLineStation(Long preStationId) {
        this.preStationId = preStationId;
    }

    public boolean hasPreStationId(Long preStationId) {
        return Objects.equals(this.preStationId, preStationId);
    }

    public boolean hasStationId(Long stationId) {
        return Objects.equals(this.stationId, stationId);
    }

    public boolean isStarting() {
        return Objects.isNull(preStationId);
    }

    public boolean isNotStarting() {
        return !isStarting();
    }

    public boolean isConnected(Long preStationId, Long stationId) {
        return (hasPreStationId(preStationId) && hasStationId(stationId))
                || (hasPreStationId(stationId) && hasStationId(preStationId));
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
