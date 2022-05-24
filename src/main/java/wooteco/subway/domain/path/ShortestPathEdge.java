package wooteco.subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.domain.Line;

public class ShortestPathEdge extends DefaultWeightedEdge {

    private final Line line;
    private final int distance;

    public ShortestPathEdge(final Line line, final int distance) {
        this.line = line;
        this.distance = distance;
    }

    public Line getLine() {
        return line;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    protected double getWeight() {
        return distance;
    }
}
