package wooteco.subway.service.infra;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionWeightEdge extends DefaultWeightedEdge {

    private final Long lineId;
    private final int distance;

    public SectionWeightEdge(Long lineId, int distance) {
        this.lineId = lineId;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    @Override
    public double getWeight() {
        return distance;
    }
}
