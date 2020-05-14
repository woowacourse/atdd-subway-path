package wooteco.subway.admin.dto;

import org.jgrapht.graph.DefaultWeightedEdge;

public class PathWeightedEdge extends DefaultWeightedEdge {
    private int distance;
    private int duration;

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
