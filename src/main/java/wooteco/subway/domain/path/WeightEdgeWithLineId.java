package wooteco.subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;

public class WeightEdgeWithLineId extends DefaultWeightedEdge {

    private final long lineId;
    private final int distance;

    public WeightEdgeWithLineId(long lineId, int distance) {
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
