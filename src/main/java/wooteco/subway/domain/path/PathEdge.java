package wooteco.subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.domain.section.Section;

public class PathEdge extends DefaultWeightedEdge {
    private final Section section;

    public PathEdge(Section section) {
        super();
        this.section = section;
    }

    @Override
    public double getWeight() {
        return section.getDistance();
    }

    public Long getLineId() {
        return section.getLineId();
    }
}
