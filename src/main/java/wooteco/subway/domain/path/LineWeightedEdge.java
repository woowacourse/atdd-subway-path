package wooteco.subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.domain.line.Line;

public class LineWeightedEdge extends DefaultWeightedEdge {

    private final Line line;
    private final int distance;

    public LineWeightedEdge(Line line, int distance) {
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
}
