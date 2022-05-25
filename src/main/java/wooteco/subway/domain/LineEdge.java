package wooteco.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class LineEdge extends DefaultWeightedEdge {

    private final long lindId;
    private final int distance;

    public LineEdge(long lindId, int distance) {
        this.lindId = lindId;
        this.distance = distance;
    }

    public long getLindId() {
        return lindId;
    }

    @Override
    protected double getWeight() {
        return distance;
    }
}
