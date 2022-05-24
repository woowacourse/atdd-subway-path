package wooteco.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class LineEdge extends DefaultWeightedEdge {

    private final Long lindId;
    private final Integer distance;

    public LineEdge(final Long lindId, final Integer distance) {
        this.lindId = lindId;
        this.distance = distance;
    }

    public Long getLindId() {
        return lindId;
    }

    @Override
    protected double getWeight() {
        return distance;
    }
}
