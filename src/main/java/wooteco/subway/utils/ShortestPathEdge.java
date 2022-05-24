package wooteco.subway.utils;

import org.jgrapht.graph.DefaultWeightedEdge;

public class ShortestPathEdge extends DefaultWeightedEdge {

    private final int extraFare;
    private final int distance;

    public ShortestPathEdge(int extraFare, int distance) {
        this.extraFare = extraFare;
        this.distance = distance;
    }

    public int getExtraFare() {
        return extraFare;
    }

    @Override
    protected double getWeight() {
        return distance;
    }
}
