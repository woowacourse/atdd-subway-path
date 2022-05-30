package wooteco.subway.domain.path;

import lombok.Getter;
import wooteco.subway.domain.section.Section;
import wooteco.subway.utils.ConcreteWeightedEdge;

public class SectionWeightedEdge extends ConcreteWeightedEdge<Long> {

    @Getter
    private final Long lineId;
    private final Integer distance;

    public SectionWeightedEdge(Section section) {
        super(section.getUpStationId(), section.getDownStationId());
        this.lineId = section.getLineId();
        this.distance = section.getDistance();
    }

    @Override
    protected double getWeight() {
        return distance;
    }
}
