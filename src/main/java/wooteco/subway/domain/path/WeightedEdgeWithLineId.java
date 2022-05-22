package wooteco.subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;

class WeightedEdgeWithLineId extends DefaultWeightedEdge {

    private final Long lineId;
    private final int distance;

    WeightedEdgeWithLineId(Long lineId, int distance) {
        this.lineId = lineId;
        this.distance = distance;
    }

    Long getLineId() {
        return lineId;
    }

    @Override
    public double getWeight() {
        return distance;
    }
}
