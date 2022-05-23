package wooteco.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {
    private int distance;
    private Line line;

    public SectionEdge(int distance, Line line) {
        this.distance = distance;
        this.line = line;
    }

    @Override
    protected double getWeight() {
        return distance;
    }
}
