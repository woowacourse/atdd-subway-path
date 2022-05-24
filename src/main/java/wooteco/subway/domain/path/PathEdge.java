package wooteco.subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;

public class PathEdge extends DefaultWeightedEdge {
    private final Fare extraFare;
    private final double distance;

    public PathEdge(Fare extraFare, double distance) {
        this.extraFare = extraFare;
        this.distance = distance;
    }

    public Fare getExtraFare() {
        return extraFare;
    }

    @Override
    protected double getWeight() {
        return distance;
    }
}
