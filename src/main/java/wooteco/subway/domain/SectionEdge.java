package wooteco.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

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
