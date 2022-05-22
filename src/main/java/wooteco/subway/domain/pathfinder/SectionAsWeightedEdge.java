package wooteco.subway.domain.pathfinder;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionAsWeightedEdge extends DefaultWeightedEdge {

    private int extraFare;
    private int distance;

    public SectionAsWeightedEdge(int extraFare, int distance) {
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
