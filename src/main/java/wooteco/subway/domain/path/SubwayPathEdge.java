package wooteco.subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.domain.section.Section;

public class SubwayPathEdge extends DefaultWeightedEdge {

    private final Section section;

    public SubwayPathEdge(Section section) {
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
