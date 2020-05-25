package wooteco.subway.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    public boolean isSameLineStation(Long preStationId, Long stationId) {
        return Objects.equals(this.preStationId, preStationId) && Objects.equals(this.stationId, stationId);
    }

    @JsonIgnore
    public LineStationKey getLineStationKey() {
        return new LineStationKey(preStationId, stationId);
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
