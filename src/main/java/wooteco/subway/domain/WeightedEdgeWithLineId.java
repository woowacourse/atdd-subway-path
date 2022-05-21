package wooteco.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class WeightedEdgeWithLineId extends DefaultWeightedEdge {

    private final Long lineId;
    private final int distance;

    public WeightedEdgeWithLineId(Long lineId, int distance) {
        this.lineId = lineId;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    @Override
    public double getWeight() {
        return distance;
    }
}
