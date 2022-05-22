package wooteco.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SubwayPathEdge extends DefaultWeightedEdge {

    private final Line line;
    private final int distance;

    private SubwayPathEdge(final Line line, final int distance) {
        this.line = line;
        this.distance = distance;
    }

    public static SubwayPathEdge from(final Section section) {
        return new SubwayPathEdge(section.getLine(), section.getDistance());
    }

    public Line getLine() {
        return line;
    }

    @Override
    protected double getWeight() {
        return distance;
    }
}
