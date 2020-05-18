package wooteco.subway.admin.domain;

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
}
