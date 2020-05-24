package wooteco.subway.admin.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class Edge extends DefaultWeightedEdge {
    private final int distance;
    private final int duration;

    public Edge(final int distance, final int duration) {
        this.distance = distance;
        this.duration = duration;
    }

    @Override
    protected double getWeight() {
        return super.getWeight();
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }
}
