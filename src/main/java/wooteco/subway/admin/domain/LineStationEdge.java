package wooteco.subway.admin.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class LineStationEdge extends DefaultWeightedEdge {

    private final LineStation lineStation;
    private final PathType pathType;

    public LineStationEdge(LineStation lineStation, PathType pathType) {
        this.lineStation = lineStation;
        this.pathType = pathType;
    }

    @Override
    protected double getWeight() {
        return pathType.getWeight(lineStation);
    }

    public int getDistance() {
        return lineStation.getDistance();
    }

    public int getDuration() {
        return lineStation.getDuration();
    }

    public LineStation getLineStation() {
        return lineStation;
    }

    public PathType getPathType() {
        return pathType;
    }
}
