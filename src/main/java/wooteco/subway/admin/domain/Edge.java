package wooteco.subway.admin.domain;

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

    public boolean isNotFirst() {
        return preStationId != null;
    }

    public boolean isSameStationId(Long stationId) {
        return this.stationId.equals(stationId);
    }

    public void updatePreLineStation(Long preStationId) {
        this.preStationId = preStationId;
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
