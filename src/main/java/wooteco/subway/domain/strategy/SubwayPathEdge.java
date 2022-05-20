package wooteco.subway.domain.strategy;

import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;

public class SubwayPathEdge extends DefaultWeightedEdge {

    private final Line line;
    private final int distance;

    public SubwayPathEdge(final Line line, final int distance) {
        this.line = line;
        this.distance = distance;
    }

    public static SubwayPathEdge from(final Section section) {
        return new SubwayPathEdge(section.getLine(), section.getDistance());
    }

    @Override
    protected double getWeight() {
        return distance;
    }
}
