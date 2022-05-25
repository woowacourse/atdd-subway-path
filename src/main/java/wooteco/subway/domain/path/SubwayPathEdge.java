package wooteco.subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.domain.Section;

public class SubwayPathEdge extends DefaultWeightedEdge {

    private final Section section;

    public SubwayPathEdge(final Section section) {
        this.section = section;
    }

    public Long getLineId() {
        return section.getLineId();
    }

    public int getDistance() {
        return section.getDistance();
    }

    @Override
    protected double getWeight() {
        return section.getDistance();
    }
}
