package wooteco.subway.domain.path.pathfinder;

import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.domain.section.Section;

public class SectionEdge extends DefaultWeightedEdge {
    private final Section section;

    public SectionEdge(Section section) {
        this.section = section;
    }

    public Long getLineId() {
        return section.getLineId();
    }

    @Override
    public String toString() {
        return "SectionEdge{" +
                "section=" + section +
                '}';
    }
}
