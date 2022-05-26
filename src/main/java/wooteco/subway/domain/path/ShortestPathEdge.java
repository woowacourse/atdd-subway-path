package wooteco.subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.domain.Line;

public class ShortestPathEdge extends DefaultWeightedEdge {

    private final Line line;
    private final int distance;

    public ShortestPathEdge(Line line, int distance) {
        this.line = line;
        this.distance = distance;
    }

    @Override
    protected double getWeight() {
        return distance;
    }

    public Line getLine() {
        return line;
    }

    public int getDistance() {
        return distance;
    }
}
