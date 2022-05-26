package wooteco.subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.domain.Line;

public class SubwayWeightEdge extends DefaultWeightedEdge {

    private final Line line;
    private final double distance;

    public SubwayWeightEdge(final Line line, final double distance) {
        this.line = line;
        this.distance = distance;
    }

    public int getExtraFare() {
        return line.getExtraFare();
    }

    @Override
    protected double getWeight() {
        return distance;
    }
}
