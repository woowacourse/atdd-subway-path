package wooteco.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class ShortestPathEdge extends DefaultWeightedEdge {

    private long lineId;
    private int distance;

    public ShortestPathEdge(final long lineId, final int distance) {
        this.lineId = lineId;
        this.distance = distance;
    }

    public long getLineId() {
        return lineId;
    }

    @Override
    protected double getWeight() {
        return distance;
    }
}
