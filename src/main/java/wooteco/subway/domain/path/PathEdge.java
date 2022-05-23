package wooteco.subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.domain.Section;

public class PathEdge extends DefaultWeightedEdge {

    private final long lineId;
    private final int distance;

    public PathEdge(long lineId, int distance) {
        this.lineId = lineId;
        this.distance = distance;
    }

    public static PathEdge from(Section section) {
        return new PathEdge(section.getLineId(), section.getDistance());
    }

    public long getLineId() {
        return lineId;
    }

    @Override
    protected double getWeight() {
        return distance;
    }
}
