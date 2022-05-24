package wooteco.subway.domain.path;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jgrapht.graph.DefaultWeightedEdge;

@RequiredArgsConstructor
public class SectionWeightedEdge extends DefaultWeightedEdge {

    @Getter
    private final Long lineId;
    private final Integer distance;

    @Override
    protected double getWeight() {
        return distance;
    }
}
