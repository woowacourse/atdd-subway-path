package wooteco.subway.admin.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class LineStation extends DefaultWeightedEdge {

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

    @Override
    protected double getWeight() {
        return distance;
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
    protected Object getSource() {
        return super.getSource();
    }

    @Override
    protected Object getTarget() {
        return this;
    }
}
