package wooteco.subway.admin.domain;

import java.util.Objects;

public class Edge {

    private Long preStationId;
    private Long stationId;
    private int distance;
    private int duration;

    public Edge(Long preStationId, Long stationId, int distance, int duration) {
        this.preStationId = preStationId;
        this.stationId = stationId;
        this.distance = distance;
        this.duration = duration;
    }

    public boolean isFirst() {
        return Objects.isNull(preStationId);
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

    public void updatePreStation(Long preStationId) {
        this.preStationId = preStationId;
    }
}
