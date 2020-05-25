package wooteco.subway.admin.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;

import wooteco.subway.admin.domain.LineStation;

public class LineStationEdge extends DefaultWeightedEdge {
    private int distance;
    private int duration;

    private LineStationEdge(int distance, int duration) {
        this.distance = distance;
        this.duration = duration;
    }

    public static LineStationEdge of(LineStation lineStation) {
        return new LineStationEdge(lineStation.getDistance(), lineStation.getDuration());
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }
}
