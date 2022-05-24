package wooteco.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class PathEdge extends DefaultWeightedEdge {
    private final Long lineId;
    private final Distance distance;

    public PathEdge(Long lineId, Distance distance) {
        this.lineId = lineId;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    @Override
    protected double getWeight() {
        return distance.getValue();
    }
}
