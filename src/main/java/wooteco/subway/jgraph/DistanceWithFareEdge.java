package wooteco.subway.jgraph;

import org.jgrapht.graph.DefaultWeightedEdge;

public class DistanceWithFareEdge extends DefaultWeightedEdge {
    private final long distance;
    private final long fare;

    public DistanceWithFareEdge(long distance, long fare) {
        this.distance = distance;
        this.fare = fare;
    }

    @Override
    protected double getWeight() {
        return super.getWeight();
    }

    public long getDistance() {
        return distance;
    }

    public long getFare() {
        return fare;
    }
}
