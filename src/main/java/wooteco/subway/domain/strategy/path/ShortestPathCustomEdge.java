package wooteco.subway.domain.strategy.path;

import org.jgrapht.graph.DefaultWeightedEdge;

public class ShortestPathCustomEdge extends DefaultWeightedEdge {

    private final Long lineId;
    private final int distance;

    public ShortestPathCustomEdge(Long lineId, int distance) {
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
