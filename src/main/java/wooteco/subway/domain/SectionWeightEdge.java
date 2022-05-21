package wooteco.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionWeightEdge extends DefaultWeightedEdge {

    private final Long lineId;
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    public SectionWeightEdge(Long lineId, Long upStationId, Long downStationId, int distance) {
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    @Override
    public double getWeight() {
        return distance;
    }
}
