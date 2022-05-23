package wooteco.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class ShortestPathEdge extends DefaultWeightedEdge {

    private final Long lineId;
    private final int distance;

    public ShortestPathEdge(Long lineId, int distance) {
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
