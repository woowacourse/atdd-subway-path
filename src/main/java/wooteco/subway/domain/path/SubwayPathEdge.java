package wooteco.subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SubwayPathEdge extends DefaultWeightedEdge {

    private final Long lineId;
    private final int distance;

    public SubwayPathEdge(final Long lineId, final int distance) {
        this.lineId = lineId;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    protected double getWeight() {
        return distance;
    }
}
