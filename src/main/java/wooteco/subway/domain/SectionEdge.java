package wooteco.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    private int distance;

    public SectionEdge addInformation(long lineId, int distance) {
        this.distance = distance;
        return this;
    }

    public int getDistance() {
        return this.distance;
    }
}
