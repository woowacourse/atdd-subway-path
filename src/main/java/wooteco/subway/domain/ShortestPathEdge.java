package wooteco.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class ShortestPathEdge extends DefaultWeightedEdge {

    private Line line;
    private int distance;

    public ShortestPathEdge(final Line line, final int distance) {
        this.line = line;
        this.distance = distance;
    }

    public Line getLine() {
        return line;
    }

    @Override
    protected double getWeight() {
        return distance;
    }
}
