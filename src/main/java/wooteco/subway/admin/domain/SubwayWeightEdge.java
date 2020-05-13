package wooteco.subway.admin.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SubwayWeightEdge extends DefaultWeightedEdge {
    public double getValue() {
        return getWeight();
    }
}
