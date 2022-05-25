package wooteco.subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.domain.element.Line;

public class LineWeightEdge extends DefaultWeightedEdge {
    private final Line line;

    public LineWeightEdge(Line line) {
        this.line = line;
    }

    public Line getLine() {
        return line;
    }
}
