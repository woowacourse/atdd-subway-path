package wooteco.subway.admin.domain;

import java.util.Objects;

public class Edge {
    private Long preStationId;
    private final Long stationId;
    private final int distance;
    private final int duration;

    public Edge(Long preStationId, Long stationId, int distance, int duration) {
        this.preStationId = preStationId;
        this.stationId = stationId;
        this.distance = distance;
        this.duration = duration;
    }

    public void updatePreEdge(Long preStationId) {
        this.preStationId = preStationId;
    }

    public boolean isEdgeOf(Long preStationId, Long stationId) {
        return (this.preStationId.equals(preStationId) && this.stationId.equals(stationId))
                || (this.preStationId.equals(stationId) && this.stationId.equals(preStationId));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(preStationId, edge.preStationId) &&
                Objects.equals(stationId, edge.stationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(preStationId, stationId);
    }
}
