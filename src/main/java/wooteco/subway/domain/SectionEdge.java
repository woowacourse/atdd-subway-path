package wooteco.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    private long lineId;
    private int distance;
    public SectionEdge addInformation(long lineId, int distance) {
        this.lineId = lineId;
        this.distance = distance;
        return this;
    }

    public long getLineId() {
        return this.lineId;
    }

    public int getDistance() {
        return this.distance;
    }
}
