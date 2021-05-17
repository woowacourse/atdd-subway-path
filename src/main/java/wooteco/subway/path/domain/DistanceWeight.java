package wooteco.subway.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class DistanceWeight extends DefaultWeightedEdge {
    public int getDistance() {
        return (int) this.getWeight();
    }
}
