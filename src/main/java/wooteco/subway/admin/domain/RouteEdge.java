package wooteco.subway.admin.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class RouteEdge extends DefaultWeightedEdge {
    private final int distance;
    private final int duration;

    public RouteEdge(int distance, int duration) {
        this.distance = distance;
        this.duration = duration;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }
}
