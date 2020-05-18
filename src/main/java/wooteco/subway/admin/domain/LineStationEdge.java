package wooteco.subway.admin.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class LineStationEdge extends DefaultWeightedEdge {
    private final LineStation lineStation;
    private final PathType pathType;

    private LineStationEdge(LineStation lineStation, PathType pathType) {
        this.lineStation = lineStation;
        this.pathType = pathType;
    }

    public static LineStationEdge of(LineStation lineStation, PathType pathType) {
        return new LineStationEdge(lineStation, pathType);
    }

    @Override
    protected double getWeight() {
        return pathType.getValue(lineStation);
    }

    public int getValueOf(PathType pathType) {
        return pathType.getValue(lineStation);
    }

    public int getDuration() {
        return lineStation.getDuration();
    }

    public int getDistance() {
        return lineStation.getDistance();
    }
}
