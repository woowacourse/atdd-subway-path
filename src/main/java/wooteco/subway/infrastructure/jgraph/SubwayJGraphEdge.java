package wooteco.subway.infrastructure.jgraph;

import org.jgrapht.graph.DefaultWeightedEdge;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.section.Distance;
import wooteco.subway.domain.path.graph.SubwayGraphEdge;

public class SubwayJGraphEdge extends DefaultWeightedEdge implements SubwayGraphEdge {

    private final Line line;
    private final Distance distance;

    public SubwayJGraphEdge(Line line, Distance distance) {
        this.line = line;
        this.distance = distance;
    }

    @Override
    protected double getWeight() {
        return distance.getDistance();
    }

    @Override
    public Line getLine() {
        return line;
    }
}
