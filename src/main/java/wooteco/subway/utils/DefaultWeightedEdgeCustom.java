package wooteco.subway.utils;

import org.jgrapht.graph.DefaultWeightedEdge;

public class DefaultWeightedEdgeCustom extends DefaultWeightedEdge {
    private final Long lineId;
    private final int distance;

    public DefaultWeightedEdgeCustom(final Long lineId, final int distance) {
        this.lineId = lineId;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    @Override
    protected double getWeight() {
        return distance;
    }
}
