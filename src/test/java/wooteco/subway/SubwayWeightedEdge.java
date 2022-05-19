package wooteco.subway;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SubwayWeightedEdge extends DefaultWeightedEdge {

    private Long lineId;

    public SubwayWeightedEdge() {
    }

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(final Long lineId) {
        this.lineId = lineId;
    }
}
