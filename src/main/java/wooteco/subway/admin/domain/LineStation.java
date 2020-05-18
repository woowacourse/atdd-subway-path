package wooteco.subway.admin.domain;

public class LineStation extends TimeEntity {
    private Long preStationId;
    private final Long stationId;
    private final int distance;
    private final int duration;

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

    @Override
    public String toString() {
        return "LineStation{" +
            "preStationId=" + preStationId +
            ", stationId=" + stationId +
            ", distance=" + distance +
            ", duration=" + duration +
            '}';
    }
}
