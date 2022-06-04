package wooteco.subway.support;

import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.domain.path.Fare;

public class PathEdge extends DefaultWeightedEdge {
    private final Fare extraFare;
    private final double distance;

    PathEdge(Fare extraFare, double distance) {
        this.extraFare = extraFare;
        this.distance = distance;
    }

    Fare getExtraFare() {
        return extraFare;
    }

    @Override
    protected double getWeight() {
        return distance;
    }
}
