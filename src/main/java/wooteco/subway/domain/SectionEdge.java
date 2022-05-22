package wooteco.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    private long lineId;

    public SectionEdge addLineInformation(long lineId) {
        this.lineId = lineId;
        return this;
    }

    public long getLineId() {
        return this.lineId;
    }
}
