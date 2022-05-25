package wooteco.subway.domain.strategy;

import org.jgrapht.graph.DefaultWeightedEdge;

public class ShortestPathEdge extends DefaultWeightedEdge {

    private Long lineId;
    private int distance;

    public ShortestPathEdge(final Long lineId, final int distance) {
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
