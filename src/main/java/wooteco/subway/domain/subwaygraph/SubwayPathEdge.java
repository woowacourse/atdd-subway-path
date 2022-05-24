package wooteco.subway.domain.subwaygraph;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SubwayPathEdge extends DefaultWeightedEdge {

    private final Long lineId;
    private final int distance;

    public SubwayPathEdge(final Long lineId, final int distance) {
        this.lineId = lineId;
        this.distance = distance;
    }

    @Override
    protected double getWeight() {
        return distance;
    }
}
