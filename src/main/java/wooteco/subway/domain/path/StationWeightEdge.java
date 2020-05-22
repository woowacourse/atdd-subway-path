package wooteco.subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;

import wooteco.subway.domain.LineStation;

public class StationWeightEdge extends DefaultWeightedEdge {
    private int distance;
    private int duration;

    public StationWeightEdge(int distance, int duration) {
        this.distance = distance;
        this.duration = duration;
    }

    public StationWeightEdge(LineStation lineStation) {
        this(lineStation.getDistance(), lineStation.getDuration());
    }

    public double getDistance() {
        return distance;
    }

    public double getDuration() {
        return duration;
    }

}
