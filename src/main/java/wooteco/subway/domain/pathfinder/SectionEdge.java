package wooteco.subway.domain.pathfinder;

import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.domain.Section;

public class SectionEdge extends DefaultWeightedEdge {

    private final Section section;

    public SectionEdge(Section section) {
        this.section = section;
    }

    @Override
    protected double getWeight() {
        return section.getDistance();
    }

    public Section getSection() {
        return section;
    }
}
